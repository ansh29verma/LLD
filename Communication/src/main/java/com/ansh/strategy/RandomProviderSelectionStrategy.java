package com.ansh.strategy;

import com.ansh.enums.ErrorCode;
import com.ansh.exceptions.CommunicationException;
import com.ansh.model.Provider;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomProviderSelectionStrategy implements SelectionStrategy {

    @Override
    public Provider selectProvider(List<Provider> providers) {
        if (providers == null || providers.isEmpty()) {
            throw new CommunicationException(ErrorCode.NO_ELIGIBLE_PROVIDER);
        }
        int randomIndex = ThreadLocalRandom.current().nextInt(providers.size());
        return providers.get(randomIndex);
    }
}
