package singleTon;

/**
 * 懒汉非线程安全单例模式
 */
public class SingleTonLazySimple {
    private static SingleTonLazySimple singleTonLazySimple;

    private SingleTonLazySimple(){}

    public static SingleTonLazySimple getSingleTonLazySimple(){
        if (singleTonLazySimple == null) {
            singleTonLazySimple = new SingleTonLazySimple();
        }
        return singleTonLazySimple;
    }

    public void showMessage() {
        System.out.println("简单懒汉单例模式");
    }
}
