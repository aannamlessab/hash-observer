/**
 * A blocking queue of size 1 with upsert put.
 */
package org.example.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SaltContainer {

    private BlockingQueue<byte[]> saltContainer = new ArrayBlockingQueue<byte[]>(1);

    /**
     * upserts the salt in container
     * @param salt
     */
    public void put(byte[] salt){
        saltContainer.poll(); //discard old salt
        saltContainer.add(salt); //insert new salt
    }

    /**
     * Retrieves and removes the salt, waiting if necessary until salt becomes available.
     * @return
     * @throws InterruptedException
     */
    public byte[] take() throws InterruptedException {
        return saltContainer.take();
    }
}
