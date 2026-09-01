package com.ansh.client;


import com.ansh.enums.ChannelType;
import com.ansh.model.Provider;
import com.ansh.request.CommunicationRequest;

public class ExternalProviderClient implements ProviderClient {

    @Override
    public boolean send(Provider provider, CommunicationRequest request) {
        ChannelType channel = request.getChannelType();
        String endpoint = provider.getEndpoints().get(channel);

        // Header injection demonstration
        String authHeader = "Basic " + provider.getCredentials().getUsername() + ":" + provider.getCredentials().getPassword();

        System.out.println(String.format("[HTTP POST] Calling Provider '%s' at URL '%s' with Auth Header '%s' for Request ID: %s",
                provider.getName(), endpoint, authHeader, request.getRequestId()));

        // Mocking successful network call
        return true;
    }
}
