package generics;

import java.util.ArrayList;
import java.util.Arrays;

public class Box<T extends Fruit> {
    // Класс Box, в который можно складывать фрукты.
    // Коробки условно сортируются по типу фрукта, поэтому в одну коробку нельзя сложить и яблоки, и апельсины

    // Для хранения фруктов внутри коробки можно использовать ArrayList
    private final ArrayList<T> storage;

    // Написать метод, который преобразует массив в ArrayList
    @SafeVarargs
    public Box(T... fruits) {
        storage = new ArrayList<>(Arrays.asList(fruits));
    }

    public Box(ArrayList<T> storage) {
        this.storage = storage;
    }

    public static void main(String[] args) {
        Box<Apple> appleBox1 = new Box<>(new Apple(1.f));
        Box<Apple> appleBox5 = new Box<>(new Apple(1.f));
        System.out.println(appleBox1.compare(appleBox5));           // true

        appleBox5.add(new Apple(4.f));
        System.out.println(appleBox1.compare(appleBox5));           // false

        Box<Orange> orangeBox4_99 = new Box<>(new Orange(4.99999f));
        Box<Orange> orangeBox4_98 = new Box<>(new Orange(2.499995f), new Orange(2.49999f));
        System.out.println(orangeBox4_99.compare(orangeBox4_98));   // true

        Box<Orange> orangeBox5 = new Box<>(new Orange(5.f));
        System.out.println(orangeBox4_98.compare(orangeBox5));      // false

        System.out.println(appleBox1.compare(orangeBox4_99));       // false
        System.out.println(appleBox5.compare(orangeBox5));          // true

        appleBox1.relocateFruitsTo(appleBox5);
        System.out.println(appleBox1);                              // empty
        System.out.println(appleBox5);                              // Apple:1.0,Apple:4.0,Apple:1.0
        // appleBox5.relocateFruitsTo(orangeBox4_91);               // ошибка компиляции
    }

    // Не забываем про метод добавления фрукта в коробку
    public void add(T fruit) {
        storage.add(fruit);
    }

    // Сделать метод getWeight(), который высчитывает вес коробки, зная вес одного фрукта и их количество:
    // вес яблока – 1.0f, апельсина – 1.5f (единицы измерения не важны)
    public float getWeight() {
        float weight = .0f;
        for (T fruit : storage) {
            weight += fruit.getWeight();
        }
        return weight;
    }

    // Внутри класса Box сделать метод compare(), который позволяет сравнить текущую коробку с той,
    // которую подадут в compare() в качестве параметра. true – если их массы равны, false в противоположном случае.
    // Можно сравнивать коробки с яблоками и апельсинами
    public boolean compare(Box<?> another) {
        if (this == another) {
            return true;
        }
        return Math.abs(this.getWeight() - another.getWeight()) <= 0.00001f;
    }

    // Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую.
    // Помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами.
    // Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты, которые были в первой
    public void relocateFruitsTo(Box<T> another) {
        another.storage.addAll(storage);
        this.storage.clear();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("[");
        for (T fruit : storage) {
            s.append(fruit).append(",");
        }
        s.deleteCharAt(s.length() - 1).append("]");
        return "Box{" +
                "storage=" + s +
                '}';
    }
}
