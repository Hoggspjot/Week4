import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLock {
    public static void main(String[] args) throws InterruptedException {

        MyDeadThreads myDeadThreads = new MyDeadThreads();

        Thread thread1 = new Thread(() -> myDeadThreads.firstThread());

        Thread thread2 = new Thread(() -> myDeadThreads.secondThread());

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        myDeadThreads.finished();

    }
}


class MyDeadThreads {
    Counter counter1 = new Counter();
    Counter counter2 = new Counter();

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();


    public void firstThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            //в этом потоке сначала блокируем лок 1 , а потом лок 2
            //но лок2 уже забрал второй поток и пытается забрать лок1
            //потоки зависли в ожидании
        lock1.lock();
        lock2.lock();
        try {
            Counter.calculate(counter1, counter2, random.nextInt(10));
        } finally {
            lock1.unlock();
            lock2.unlock();
        }
        }
    }

    public void secondThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            //здесь второй поток сначала забирает лок2 и пытается забрать лок 1 , который
            //уже забрал первый поток
            lock2.lock();
            lock1.lock();
            try {
                Counter.calculate(counter2, counter1, random.nextInt(10));
            }finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void finished() {
        System.out.println(counter1.getCount());
        System.out.println(counter2.getCount());
        System.out.println("Total numbers = "+ (counter1.getCount()+ counter2.getCount()));
        if (counter1.getCount() > counter2.getCount()) {
            System.out.println("Первый счетчик победил");
        }else if (counter1.getCount() == counter2.getCount()) {
            System.out.println("Ничья");
        }else {
            System.out.println("Второй счетчик победил");
        }


    }

}

class Counter {
    private int count = 1000;

    public void plus(int plus) {
        count += plus;
    }

    public void minus(int minus) {
        count -= minus;
    }

    public int getCount() {
        return count;
    }

    public static void calculate(Counter c1, Counter c2, int number) {
        c1.plus(number);
        c2.minus(number);
    }

}

