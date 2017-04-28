package io.hgc.jarspec.fixtures.async;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing long-running asynchronous work
 */
class AsyncWork {
    private int value;

    public int getResult() {
        return value;
    }

    protected void process(int processingTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                value = 1;
            }
        }, processingTime);
    }
}
