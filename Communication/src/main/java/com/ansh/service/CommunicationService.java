package com.ansh.service;

import com.ansh.model.Provider;
import com.ansh.request.CommunicationRequest;

public interface CommunicationService {

    void addProvider(Provider provider);
    Provider getProvider(String providerId);
    void updateState(String providerId, boolean active);
    void updateProvider(Provider provider);
    void processRequest(CommunicationRequest request);
    void handleCallback(String requestId, String status);
}
