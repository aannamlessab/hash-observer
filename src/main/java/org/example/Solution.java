package org.example;

import org.example.observers.HashObserver;
import org.example.util.HashUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Solution {
    private Source source;
    private Sink sink;

    public Solution(Source source, Sink sink) {
        this.source = source;
        this.sink = sink;
    }

    public void start() {
        Observer observer = createObserver(sink);
        source.subscribe(observer);
    }

    protected abstract Observer createObserver(Sink sink);

    public static Observer createHashObserver(HashUtil.HASH_ALGORITHM hashAlgorithm, int nThreads, int repetition, Sink sink) {
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        return new HashObserver(hashAlgorithm, repetition, executorService, sink);
    }
}

