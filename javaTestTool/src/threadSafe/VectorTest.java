package threadSafe;

import java.util.Vector;

/**
 * 测试Vector的线程安全
 */
public class VectorTest{
    public static void main(String[] args) throws InterruptedException {

        Vector<Integer> integerVector = new Vector<>();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    integerVector.add(i);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    integerVector.add(i);

                }
            }
        });
        thread1.start();
        thread2.start();
        System.out.println("vector.size:" + integerVector.size());
        thread2.sleep(3000);
        System.out.println("vector.size:" + integerVector.size());
        thread1.join();
        System.out.println("vector.size:" + integerVector.size());
        thread2.join();
        System.out.println("vector.size:" + integerVector.size());


    }
}
