package com.ansh.strategy;

import com.ansh.model.Provider;

import java.util.List;

public interface SelectionStrategy {
    Provider selectProvider(List<Provider> providers);
}
