package com.ansh.client;


import com.ansh.model.Provider;
import com.ansh.request.CommunicationRequest;

public interface ProviderClient {
    boolean send(Provider provider, CommunicationRequest request);
}
