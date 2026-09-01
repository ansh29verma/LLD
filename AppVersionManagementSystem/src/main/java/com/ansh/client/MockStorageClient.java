package com.ansh.client;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockStorageClient implements StorageClient {
    private final Map<String, byte[]> storage = new ConcurrentHashMap<>();

    @Override
    public String uploadFile(byte[] fileContent) {
        String url = "https://cdn.store.com/files/" + System.nanoTime();
        storage.put(url, fileContent);
        return url;
    }

    @Override
    public byte[] getFile(String fileUrl) {
        return storage.getOrDefault(fileUrl, new byte[0]);
    }

    @Override
    public byte[] createDiffPack(byte[] sourceFile, byte[] targetFile) {
        String diffMarker = "[DIFF-PACK: " + sourceFile.length + " -> " + targetFile.length + "]";
        return diffMarker.getBytes();
    }
}
