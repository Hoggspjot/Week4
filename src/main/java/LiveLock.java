import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LiveLock {
    public static void main(String[] args) {



    }
}


class MyLiveLocks {
    LiveCounter liveCounter1 = new LiveCounter();
    LiveCounter liveCounter2 = new LiveCounter();

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();


    public void firstThread() {

    }

    public void secondThread() {

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

