package singleTon;

/**
 * 学习简单单例模式
 */
public class SingleTon {
    // 创建对象
    private static SingleTon singleton = new SingleTon();

    // 私有化构造方法
    private SingleTon() {}

    // 获取唯一对象
    public static SingleTon getSingleton () {
        return singleton;
    }

    public void showMessage() {
        System.out.println("单例模式的输出方法");
    }
}
