package safety;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class JavaCounter {
    public static void main(String[] args) throws InterruptedException {
        Counter2 counter2 = new Counter2();
        Executor executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 50; i++) {
            executor.execute(() -> {
                try {
                    for(int i1 = 0; i1 < 20; i1++){
                        counter2.decrement();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        Thread.sleep(4000);
        System.out.println(counter2.getCount());


    }
}

class Counter2 {
    private AtomicInteger atomicInteger = new AtomicInteger(1000);

    void decrement() throws InterruptedException {
        if (atomicInteger.get() > 0) {
            Thread.sleep(10);
            atomicInteger.decrementAndGet();
        }


    }

    public int getCount() {
        return atomicInteger.get();
    }
}
