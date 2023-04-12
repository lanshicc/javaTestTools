package singleTon;

/**
 * 线程安全的懒汉单例模式
 */
public class SingleTonLazySafe {
    private static SingleTonLazySafe singleTonLazySafe;

    private SingleTonLazySafe(){}

    public static synchronized SingleTonLazySafe getSingleTonLazySafe(){
        if (singleTonLazySafe == null) {
            singleTonLazySafe = new SingleTonLazySafe();
        }
        return singleTonLazySafe;
    }

    public void showMessage() {
        System.out.println("线程安全的懒汉单例模式");
    }
}
