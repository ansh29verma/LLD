package com.ansh.client;

public interface StorageClient {
    String uploadFile(byte[] fileContent);
    byte[] getFile(String fileUrl);
    byte[] createDiffPack(byte[] sourceFile, byte[] targetFile);
}
