package anonymousClasses;

/**
 * 定义接口
 */
public interface Student {
    default void say() {
        System.out.println("我是学生");
    }

    void study();
}
