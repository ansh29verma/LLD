package com.ansh.locking;

public class Expiry {
    final long deadline;
    final String owner;

    public Expiry(long deadline, String owner) {
        this.deadline = deadline;
        this.owner = owner;
    }
}
