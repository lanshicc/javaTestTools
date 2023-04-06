/**
 * 测试synchronized关键字的使用
 */
public class SyncTest implements Runnable {
    // 共享资源
    private static int i = 0;

    // 1. 修饰普通同步方法
    /*private synchronized void add() {
       i++;
    }*/

    // 2. 修饰静态方法
    private static synchronized void add() {
        i++;
    }

    @Override
    public void run() {
        // 1, 2
        /*for (int j = 0; j < 10000; j++) {
            add();
        }*/

        // 3. 修饰同步代码块
        synchronized (this) {
            for (int j = 0; j < 10000; j++) {
                i++;

            }
        }

    }

    public static void main(String[] args) throws InterruptedException {

        // 1. 修饰普通同步方法 3. 修饰同步代码块
        SyncTest syncTest = new SyncTest();
        Thread t1 = new Thread(syncTest);
        Thread t2 = new Thread(syncTest);

        // 2.修饰静态方法
        /*Thread t1 = new Thread(new SyncTest());
        Thread t2 = new Thread(new SyncTest());*/

        t1.start();
        t2.start();

        // Thread.join()：主线程等待该线程终止
        t1.join();
        t2.join();

        System.out.println(i);
    }
}
