package com.ansh.repository;

import com.ansh.enums.ChannelType;
import com.ansh.model.Account;
import com.ansh.model.Provider;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryProviderRepository implements ProviderRepository {
    private final Map<String, Provider> providerStorage = new ConcurrentHashMap<>();

    @Override
    public Provider save(Provider provider) {
        providerStorage.put(provider.getId(), provider);
        return provider;
    }

    @Override
    public Optional<Provider> findById(String id) {
        return Optional.ofNullable(providerStorage.get(id));
    }

    @Override
    public List<Provider> findAll() {
        return List.copyOf(providerStorage.values());
    }

    @Override
    public List<Provider> findEligibleProviders(ChannelType channel, String accountId) {
        return providerStorage.values().stream()
                .filter(Provider::isActive)
                .filter(provider -> provider.getEndpoints() != null && provider.getEndpoints().containsKey(channel))
                .filter(provider -> {
                    if (accountId == null) return true;
                    if (provider.getAccounts() == null) return false;
                    Account account = provider.getAccounts().get(accountId);
                    return account != null && account.isActive() &&
                            account.getSupportedChannels() != null &&
                            account.getSupportedChannels().contains(channel);
                })
                .collect(Collectors.toList());
    }
}
