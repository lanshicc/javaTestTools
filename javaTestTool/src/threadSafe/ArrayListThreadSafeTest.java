package threadSafe;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayListThreadSafeTest{
    public static void main(String[] args) throws InterruptedException {
        // 非安全
//        List list = new ArrayList<Integer>()
        // 安全
//        List list = Collections.synchronizedList(new ArrayList<Integer>());
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        ListAdd listAdd = new ListAdd(list);
        Thread thread1 = new Thread(listAdd);
        Thread thread2 = new Thread(listAdd);
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("list.size:" + list.size());

    }
}

class ListAdd implements Runnable {
    private List list;
    public ListAdd(List list) {
        this.list = list;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
    }
}
