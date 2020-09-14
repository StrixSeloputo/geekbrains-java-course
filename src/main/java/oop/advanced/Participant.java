package oop.advanced;

public interface Participant {
    // Продолжаем работать с участниками и выполнением действий.
    // Создайте три класса Человек, Кот, которые не наследуются от одного класса.
    // Эти классы должны уметь бегать и прыгать, все также с выводом информации о действии в консоль.
    boolean isOnRace();

    boolean run(Track track);

    boolean jump(Wall wall);
}
