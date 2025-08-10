import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LiveLock {
    public static void main(String[] args) throws InterruptedException {

        MyLiveLocks myLiveLocks = new MyLiveLocks();

        Thread thread1 = new Thread(() -> myLiveLocks.firstThread());
        Thread thread2 = new Thread(() -> myLiveLocks.secondThread());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        myLiveLocks.finish();
    }
}


class MyLiveLocks {
    LiveCounter liveCounter1 = new LiveCounter();
    LiveCounter liveCounter2 = new LiveCounter();
    Random random = new Random();

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    private void takeLocks(Lock lock1, Lock lock2) {
        while (true) {
            boolean firstLockTaken = false;
            boolean secondLockTaken = false;
            try {
                firstLockTaken = lock1.tryLock();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.printf("Я %s - взял первый лок, пытаюсь взять второй\n", Thread.currentThread().getName());
                secondLockTaken = lock2.tryLock();
            } finally {
                if (firstLockTaken && secondLockTaken) {
                    System.out.println("Взял второй лок! Оба лока мои!");
                    return;
                } else {
                    if (firstLockTaken) {
                        System.out.println("Второй лок занят, отдаю первый, зачем мне один");
                        lock1.unlock();
                    }
                    if (secondLockTaken) {
                        System.out.println("Первый лок занят, отдаю второй, зачем мне один");
                        lock2.unlock();
                    }
                }
            }
        }

    }

    public void firstThread() {
        takeLocks(lock1, lock2);
        LiveCounter.result(liveCounter1,liveCounter2, random.nextInt(10) );
        lock1.unlock();
        lock2.unlock();
    }

    public void secondThread() {
        takeLocks(lock2, lock1);
        LiveCounter.result(liveCounter2,liveCounter1, random.nextInt(10) );
        lock2.unlock();
        lock1.unlock();
    }

    public void finish() {


    }

}


class LiveCounter {
    private int count = 100;

    public void plus(int number) {
        count += number;
    }

    public void minus(int number) {
        count -= number;
    }

    public int getCount() {
        return count;
    }

    public static void result(LiveCounter lc1, LiveCounter lc2, int number) {
        lc1.plus(number);
        lc2.minus(number);
    }
}

