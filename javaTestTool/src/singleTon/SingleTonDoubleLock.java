package singleTon;

/**
 * 双重校验的单例模式
 */
public class SingleTonDoubleLock {
    private static volatile SingleTonDoubleLock singleTonDoubleLock;

    private SingleTonDoubleLock(){}

    public static SingleTonDoubleLock getSingleTonDoubleLock() {
        if(singleTonDoubleLock == null) {
            synchronized (SingleTonDoubleLock.class) {
                if (singleTonDoubleLock == null) {
                    singleTonDoubleLock = new SingleTonDoubleLock();
                }
            }
        }
        return singleTonDoubleLock;
    }

    public void shouMessage() {
        System.out.println("双重校验单例模式");
    }
}
