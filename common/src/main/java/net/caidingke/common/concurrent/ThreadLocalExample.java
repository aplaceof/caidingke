package net.caidingke.common.concurrent;

/**
 * Created by bowen on 16/5/26.
 */
public class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>(){

        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static class MyRunnable implements Runnable {

        @Override
        public void run() {
            threadLocal.set((int) (Math.random() * 100));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            System.out.println(threadLocal.get());
        }
    }

    static class MyThread implements Runnable {
        private int index;

        public MyThread(int index) {
            this.index = index;
        }

        public void run() {
            System.out.println("线程" + index + "的初始threadLocal:" + threadLocal.get());
            for (int i = 0; i < 10; i++) {
                threadLocal.set(threadLocal.get() + i);
            }
            System.out.println("线程" + index + "的累加threadLocal:" + threadLocal.get());
        }
    }
}
