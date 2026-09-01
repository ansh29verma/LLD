package com.ansh.repository;

import com.ansh.enums.ChannelType;
import com.ansh.model.Provider;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository {
    Provider save(Provider provider);
    Optional<Provider> findById(String id);
    List<Provider> findAll();
    List<Provider> findEligibleProviders(ChannelType channel, String accountId);
}
