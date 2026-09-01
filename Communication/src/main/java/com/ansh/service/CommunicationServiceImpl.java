package com.ansh.service;



import com.ansh.client.ProviderClient;
import com.ansh.enums.ErrorCode;
import com.ansh.enums.RequestStatus;
import com.ansh.exceptions.CommunicationException;
import com.ansh.model.CommunicationLog;
import com.ansh.model.Provider;
import com.ansh.repository.InMemoryCommunicationLogRepository;
import com.ansh.repository.ProviderRepository;
import com.ansh.request.CommunicationRequest;
import com.ansh.strategy.RandomProviderSelectionStrategy;

import java.util.List;

public class CommunicationServiceImpl implements CommunicationService {

    private final ProviderRepository providerRepository;
    private final InMemoryCommunicationLogRepository logRepository;
    private final ProviderClient providerClient;
    private final RandomProviderSelectionStrategy selectionStrategy;

    public CommunicationServiceImpl(ProviderRepository providerRepository,
                                    InMemoryCommunicationLogRepository logRepository,
                                    ProviderClient providerClient,
                                    RandomProviderSelectionStrategy selectionStrategy) {
        this.providerRepository = providerRepository;
        this.logRepository = logRepository;
        this.providerClient = providerClient;
        this.selectionStrategy = selectionStrategy;
    }

    @Override
    public void addProvider(Provider provider) {
        validateProvider(provider);
        if (providerRepository.findById(provider.getId()).isPresent()) {
            throw new CommunicationException(ErrorCode.PROVIDER_ALREADY_EXISTS, "Provider with ID " + provider.getId() + " already exists.");
        }
        providerRepository.save(provider);
    }

    @Override
    public Provider getProvider(String providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new CommunicationException(ErrorCode.PROVIDER_NOT_FOUND, "Provider ID " + providerId + " not found."));
    }

    @Override
    public void updateState(String providerId, boolean active) {
        Provider provider = getProvider(providerId);
        provider.setActive(active);
        providerRepository.save(provider);
    }

    @Override
    public void updateProvider(Provider provider) {
        validateProvider(provider);
        getProvider(provider.getId()); // Ensures existence
        providerRepository.save(provider);
    }

    @Override
    public void processRequest(CommunicationRequest request) {
        if (request == null) {
            throw new CommunicationException(ErrorCode.INVALID_REQUEST, "Request cannot be null.");
        }

        // 1. Validate payload
        request.validate();

        // 2. Fetch eligible active providers
        List<Provider> eligibleProviders = providerRepository.findEligibleProviders(request.getChannelType(), request.getAccountId());

        // 3. Select a provider via strategy
        Provider selectedProvider = selectionStrategy.selectProvider(eligibleProviders);

        // 4. Log intent
        CommunicationLog log = CommunicationLog.builder()
                .requestId(request.getRequestId())
                .providerId(selectedProvider.getId())
                .channelType(request.getChannelType())
                .accountId(request.getAccountId())
                .status(RequestStatus.PENDING)
                .build();
        logRepository.save(log);

        // 5. Dispatch
        boolean isSuccess = providerClient.send(selectedProvider, request);

        if (isSuccess) {
            logRepository.updateStatus(request.getRequestId(), RequestStatus.SENT);
        } else {
            logRepository.updateStatus(request.getRequestId(), RequestStatus.FAILED);
            throw new CommunicationException(ErrorCode.PROVIDER_CALL_FAILED);
        }
    }

    @Override
    public void handleCallback(String requestId, String status) {
        logRepository.findByRequestId(requestId).ifPresentOrElse(
                log -> {
                    try {
                        RequestStatus newStatus = RequestStatus.valueOf(status.toUpperCase());
                        logRepository.updateStatus(requestId, newStatus);
                        System.out.println(String.format("[CALLBACK] Request %s updated to status: %s", requestId, newStatus));
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid status received in callback: " + status);
                    }
                },
                () -> System.out.println("Callback received for non-existent request ID: " + requestId)
        );
    }

    private void validateProvider(Provider provider) {
        if (provider == null || provider.getId() == null || provider.getId().isBlank() ||
                provider.getEndpoints() == null || provider.getEndpoints().isEmpty() ||
                provider.getCredentials() == null) {
            throw new CommunicationException(ErrorCode.INVALID_PROVIDER_DATA);
        }
    }
}
