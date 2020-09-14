package collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PhoneBook {
    // Написать простой класс Телефонный Справочник, который хранит в себе список фамилий и телефонных номеров.

    // Желательно не добавлять лишний функционал (дополнительные поля (имя, отчество, адрес),
    // взаимодействие с пользователем через консоль и т.д).
    // Консоль использовать только для вывода результатов проверки телефонного справочника.

    private final Map<String, Set<Long>> storage = new HashMap<>();

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        phoneBook.add("Ivanov", 88_009_000_900L);
        phoneBook.add("Smirnov", 88_009_000_901L);
        phoneBook.add("Sobolev", 88_009_000_902L);
        phoneBook.add("Petrov", 88_009_000_903L);
        phoneBook.add("Ivanov", 88_009_000_904L);
        phoneBook.add("Ivanov", 88_009_000_905L);
        System.out.println(phoneBook);
        System.out.println(phoneBook.get("Ivanov"));
        System.out.println(phoneBook.get("Smirnov"));
        System.out.println(phoneBook.get("Makarov"));
    }

    // В этот телефонный справочник с помощью метода add() можно добавлять записи.
    public void add(String surname, long phoneNumber) {
        Set<Long> phones = storage.get(surname);
        if (phones != null) {
            phones.add(phoneNumber);
        } else {
            phones = new TreeSet<>();
            phones.add(phoneNumber);
            storage.put(surname, phones);
        }
    }

    // А с помощью метода get() искать номер телефона по фамилии.
    // Следует учесть, что под одной фамилией может быть несколько телефонов (в случае однофамильцев),
    // тогда при запросе такой фамилии должны выводиться все телефоны.
    public Set<Long> get(String surname) {
        return storage.get(surname);
    }

    @Override
    public String toString() {
        return "PhoneBook{" +
                "storage=" + storage +
                '}';
    }
}
