package com.ansh.repository;

import com.ansh.enums.RequestStatus;
import com.ansh.model.CommunicationLog;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCommunicationLogRepository {
    private final Map<String, CommunicationLog> logStorage = new ConcurrentHashMap<>();

    public void save(CommunicationLog log) {
        logStorage.put(log.getRequestId(), log);
    }

    public Optional<CommunicationLog> findByRequestId(String requestId) {
        return Optional.ofNullable(logStorage.get(requestId));
    }

    public void updateStatus(String requestId, RequestStatus status) {
        CommunicationLog log = logStorage.get(requestId);
        if (log != null) {
            log.setStatus(status);
        }
    }
}
