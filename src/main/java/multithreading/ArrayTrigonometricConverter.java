package multithreading;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArrayTrigonometricConverter {
    // Необходимо написать два метода, которые делают следующее:
    //1) Создают одномерный длинный массив, например:
    private static final int SIZE = 10_000_000;
    private static final int HALF = SIZE / 2;

    // Первый просто бежит по массиву и вычисляет значения.
    public static float[] sumInSingleThread() {
        float[] arr = new float[SIZE];
        // 2) Заполняют этот массив единицами.
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }

        //3) Засекают время выполнения:
        long startTime = System.currentTimeMillis();

        //4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        //5) Проверяется время окончания метода
        long endTime = System.currentTimeMillis();

        //6) В консоль выводится время работы:
        System.out.println("1st: " + (endTime - startTime));
        return arr;
    }

    // Второй разбивает массив на два массива, в двух потоках высчитывает новые значения
    // и потом склеивает эти массивы обратно в один.
    public static float[] sumInTwoThreads() throws ExecutionException, InterruptedException {
        float[] arr = new float[SIZE];
        // 2) Заполняют этот массив единицами.
        for (int i = 0; i < SIZE; i++) {
            arr[i] = 1;
        }

        //3) Засекают время выполнения:
        long startTime = System.currentTimeMillis();

        //4) Проходят по всему массиву и для каждой ячейки считают новое значение по формуле:
        float[] arr1 = new float[HALF];
        System.arraycopy(arr, 0, arr1, 0, HALF);

        float[] arr2 = new float[SIZE - HALF];
        System.arraycopy(arr, HALF, arr2, 0, SIZE - HALF);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future future1 = executorService.submit(() -> sumInOneOfThreads(0, arr1));
        Future future2 = executorService.submit(() -> sumInOneOfThreads(HALF, arr2));

        future1.get();
        future2.get();
        executorService.shutdown();

        System.arraycopy(arr1, 0, arr, 0, HALF);
        System.arraycopy(arr2, 0, arr, HALF, SIZE - HALF);

        //5) Проверяется время окончания метода
        long endTime = System.currentTimeMillis();

        //6) В консоль выводится время работы:
        System.out.println("2st: " + (endTime - startTime));
        return arr;
    }

    private static void sumInOneOfThreads(int offset, float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + (i + offset) / 5) *
                    Math.cos(0.2f + (i + offset) / 5) * Math.cos(0.4f + (i + offset) / 2));
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        for (int j = 0; j < 10; j++) {
            float[] res1 = ArrayTrigonometricConverter.sumInSingleThread();
            float[] res2 = ArrayTrigonometricConverter.sumInTwoThreads();

            for (int i = 0; i < res1.length; i++) {
                if (res1[i] != res2[i]) {
                    System.out.println("ERR: Values doesn't equal: " + i + " : " + res1[i] + " : " + res2[i]);
                    break;
                }
            }
        }
    }
}
