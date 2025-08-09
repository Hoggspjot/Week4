

public class TwoThreads {
    public static final Object lock = new Object();
    public static boolean firstPrint = true;

    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (true) {
                            synchronized (lock) {
                                while (!firstPrint) {
                                    try {
                                        lock.wait();
                                    } catch (InterruptedException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                System.out.println(1);
                                firstPrint = false;
                                lock.notify();
                            }
                            //добавлена задержка в 1 сек для восприятия в консоли
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
        );
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock) {
                        while (firstPrint) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println(2);
                        firstPrint = true;
                        lock.notify();
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });

        thread1.start();
        thread2.start();


    }
}



