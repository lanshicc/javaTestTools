package reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * 测试反射方法
 */
public class TestReflection {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {

        ProgramMonkey programMonkey = new ProgramMonkey("大勇", "男",18);
        // 获取类名称
        Class<? extends ProgramMonkey> aClass = programMonkey.getClass();
        System.out.println("类名称:" + aClass.getName());
        // 获取父类名称
        Class<?> superclass = aClass.getSuperclass();
        System.out.println("父类名称:" + superclass.getName());
        System.out.println("简单父类名称:" + superclass.getSimpleName());

        // 获取接口
        Class<?>[] interfaces = aClass.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            System.out.println("接口: " + anInterface.getSimpleName());
        }

        // 获取方法 setMLanguage
        Method setMLanguage = programMonkey.getClass().getMethod("setmLanguage", String.class);
        // 取消反射对象使用时的JAVA语言访问检查 忽略修饰符
        setMLanguage.setAccessible(true);
        // 使用该方法赋值 Java
        setMLanguage.invoke(programMonkey, "Java");
        System.out.println(programMonkey.getmLanguage());

        // 获取属性 mLanguage
        Field mLanguage = programMonkey.getClass().getDeclaredField("mLanguage");
        // 修改属性
        mLanguage.set(programMonkey, "Python");
        System.out.println(programMonkey.getmLanguage());

        // 批量获取属性
        Field[] declaredFields = programMonkey.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("属性名称:"  + declaredField.getName());
        }

        // 批量获取子类方法
        Method[] declaredMethods = programMonkey.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            // 获取方法返回值类型
            System.out.println("方法名称:" + declaredMethod.getName() + "  返回值类型: " + declaredMethod.getReturnType());

            // 获取方法是否private
            System.out.println("是否私有:" + Modifier.isPrivate(declaredMethod.getModifiers()));
            // 如果方法私有 忽略修饰符
            if (Modifier.isPrivate(declaredMethod.getModifiers())) {
                declaredMethod.setAccessible(true);
            }

            // 获取参数类型和参数名称
            Parameter[] parameters = declaredMethod.getParameters();
            for (Parameter parameter : parameters) {
                System.out.println("参数名称:" + parameter.getName() + "  参数类型：" + parameter.getType().getName());
            }

            int parameterCount = declaredMethod.getParameterCount();

            // 执行方法
            if (parameterCount == 0) {
                Object result = declaredMethod.invoke(programMonkey);
                System.out.println("方法执行结果： " + result);
            } else {
                declaredMethod.invoke(programMonkey, "php");
            }
            System.out.println("--------------");
        }

        System.out.println("**********************");
        // 获取子类和父类的所有方法
        Method[] methods = programMonkey.getClass().getMethods();
        for (Method method : methods) {
            System.out.println("方法名:" + method.getName());
        }
    }
}
