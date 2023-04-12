package singleTon;

/**
 * 单例模式饿汉加载模式
 */
public class SingleTonHunger {
    private static SingleTonHunger singleTonHunger = new SingleTonHunger();

    private SingleTonHunger(){}

    public static  SingleTonHunger getSingleTonHunger() {
        return singleTonHunger;
    }

    public  void showMessage() {
        System.out.println("饿汉加载模式");
    }
}
