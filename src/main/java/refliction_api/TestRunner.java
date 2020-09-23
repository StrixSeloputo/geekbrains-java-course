package refliction_api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRunner {
    private static final int BEFORE_SUITE_PRIOR = 0;
    private static final int AFTER_SUITE_PRIOR = 11;
    // Создать класс, который может выполнять «тесты».

    // В качестве тестов выступают классы с наборами методов, снабженных аннотациями @Test.

    // Класс, запускающий тесты, должен иметь статический метод start(Class testClass),
    // которому в качестве аргумента передается объект типа Class.
    public static void start(Class testClass) {
        Object testObject;

        try {
            Constructor constructor = testClass.getConstructor();
            testObject = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Class with tests must have constructor without parameters");
        }


        // К каждому тесту необходимо добавить приоритеты (int-числа от 1 до 10),
        // в соответствии с которыми будет выбираться порядок их выполнения.
        // Если приоритет одинаковый, то порядок не имеет значения.
        Map<Integer, List<Method>> methodMap = new HashMap<>();
        for (Method method : testClass.getDeclaredMethods()) {
            // Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite, если он присутствует.
            // Далее запускаются методы с аннотациями @Test, а по завершении всех тестов – метод с аннотацией @AfterSuite.

            // Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре.
            // Если это не так – необходимо бросить RuntimeException при запуске «тестирования».
            if (method.getAnnotation(BeforeSuite.class) != null) {
                if (method.getParameterTypes().length > 0) {
                    throw new RuntimeException("Method with the @BeforeSuite annotation cannot have arguments");
                }

                List<Method> beforeSuite = methodMap.get(BEFORE_SUITE_PRIOR);

                if (beforeSuite != null) {
                    throw new RuntimeException("There can't be more than one method with the @BeforeSuite");
                }
                beforeSuite = new ArrayList<>(1);
                beforeSuite.add(method);
                methodMap.put(BEFORE_SUITE_PRIOR, beforeSuite);
            }

            if (method.getAnnotation(AfterSuite.class) != null) {
                if (method.getParameterTypes().length > 0) {
                    throw new RuntimeException("Method with the @AfterSuite annotation cannot have arguments");
                }

                List<Method> afterSuite = methodMap.get(AFTER_SUITE_PRIOR);

                if (afterSuite != null) {
                    throw new RuntimeException("There can't be more than one method with the @AfterSuite");
                }
                afterSuite = new ArrayList<>(1);
                afterSuite.add(method);
                methodMap.put(AFTER_SUITE_PRIOR, afterSuite);
            }


            if (method.getAnnotation(Test.class) != null) {
                if (method.getParameterTypes().length > 0) {
                    throw new RuntimeException("Method with the @Test annotation cannot have arguments");
                }

                int prior = method.getAnnotation(Test.class).prior();

                if (prior <= BEFORE_SUITE_PRIOR || prior >= AFTER_SUITE_PRIOR) {
                    throw new RuntimeException(
                            "Argument prior of the @Test annotation must be integer value in the interval from 1 to 10," +
                                    "but it is " + prior
                    );
                }

                List<Method> methodsByPrior = methodMap.getOrDefault(prior, new ArrayList<>());
                methodsByPrior.add(method);
                methodMap.put(prior, methodsByPrior);

            }
        }

        for (Map.Entry<Integer, List<Method>> entry : methodMap.entrySet()) {
            for (Method method : entry.getValue()) {

                int modifiers = method.getModifiers();

                if (Modifier.isPrivate(modifiers)) {
                    method.setAccessible(true);
                }

                try {
                    if (Modifier.isStatic(modifiers)) {
                        method.invoke(null);
                    } else {
                        method.invoke(testObject);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        TestRunner.start(TestClass.class);
    }

}
