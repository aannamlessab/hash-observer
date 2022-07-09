package org.example.observers;

import org.example.util.HashUtil;
import org.example.util.HashUtil.HASH_ALGORITHM;
import org.example.Observer;
import org.example.Sink;
import org.example.util.SaltContainer;

import java.security.MessageDigest;
import java.util.concurrent.*;

public class HashObserver implements Observer {

    private final ExecutorService threadPool;
    private final Sink sink;
    private final HASH_ALGORITHM hashAlgorithm;
    private final int repetition;
    //where we keep the latest salt
    private final SaltContainer saltContainer = new SaltContainer();

    public HashObserver(HASH_ALGORITHM hashAlgorithm, int repetition, ExecutorService threadPool, Sink sink) {
        this.hashAlgorithm = hashAlgorithm;
        this.repetition = repetition;
        this.threadPool = threadPool;
        this.sink = sink;
    }

    @Override
    public void onSalt(byte[] salt) {
        saltContainer.put(salt); //upsert salt
    }

    @Override
    public void onMessage(long id, byte[] message) {
        MessageDigest md = HashUtil.createMessageDigest(hashAlgorithm);
        //async call to hashMessage
        threadPool.submit(() -> hashMessage(md, id, message));
    }


    private void hashMessage(MessageDigest md, long id, byte[] message) throws RuntimeException {
        byte[] salt;
        try {
            //get salt, blocking call, will await for salt to be available
            salt = saltContainer.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }

        byte[] hash = HashUtil.repeatedHashWithSalt(md, repetition, message, salt);
        sink.publishHash(id, message, salt, hash);
    }

}
