package singleTon;

public class SingleTonTest {
    public static void main(String[] args) {
        // 简单单例模式
        SingleTon singleton = SingleTon.getSingleton();
        singleton.showMessage();
        SingleTonLazySimple.getSingleTonLazySimple().showMessage();
        SingleTonLazySafe.getSingleTonLazySafe().showMessage();
        SingleTonHunger.getSingleTonHunger().showMessage();
        SingleTonDoubleLock.getSingleTonDoubleLock().shouMessage();
    }
}
