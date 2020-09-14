package generics;

import java.util.ArrayList;
import java.util.Arrays;

public class GenericArrayManipulation {

    // Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа)
    public static <T> T[] replaceElements(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        return array;
    }

    // Написать метод, который преобразует массив в ArrayList
    public static <T> ArrayList<T> asArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static void main(String[] args) {
        Fruit[] array = new Fruit[3];
        array[0] = new Fruit(1.f);
        array[1] = new Orange(2.f);
        array[2] = new Apple(3.f);
        Box<Fruit> wrongBox1 = new Box<>(GenericArrayManipulation.asArrayList(array)); // Fruit:1.0,Orange:2.0,Apple:3.0
        System.out.println(wrongBox1);

        GenericArrayManipulation.replaceElements(array, 0, 2);
        Box<Fruit> wrongBox2 = new Box<>(GenericArrayManipulation.asArrayList(array)); // Apple:3.0,Orange:2.0,Fruit:1.0
        System.out.println(wrongBox2);
    }
}
