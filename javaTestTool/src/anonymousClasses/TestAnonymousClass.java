package anonymousClasses;

/**
 * 测试匿名内部类
 */
public class TestAnonymousClass {

    public static void main(String[] args) {
        Animal animal = new Animal() {
            @Override
            public void run() {
                System.out.println("奔跑");
            }
        };
        animal.run();
        animal.eat();

        // 多态向上转型
        Student student = new Student() {
            @Override
            public void study() {
                System.out.println("学习");
            }
        };
        student.say();
        student.study();
        // lambda写法
        Student student1 = () ->  {
            System.out.println("学习一下");
        };
        student1.study();
    }

}
