package oop.base;

public class TaskTracker {
    // Создайте класс TaskTracker, который хранит в себе массив из 10 задач.
    private Task[] tasks = new Task[10];
    private int count;

    // Этот класс должен позволять добавлять задачу в массив.
    // Если весь массив заполнен, то при попытке добавить новую задачу в консоль,
    // необходимо вывести сообщение: “Список задач заполнен”.
    public boolean addTask(String name, String reporter, String assignee, String description, String status) {
        if (count == tasks.length) {
//            System.out.println("Список задач заполнен");
            System.out.println("Task list is full");
            return false;
        }
        tasks[count++] = new Task(name, reporter, assignee, description, status);
        return true;
    }

    // TaskTracker должен предоставлять возможность распечатывать список задач.
    public void printAll() {
        for (int i = 0; i < count; i++) {
            System.out.println(tasks[i]);
        }
    }

    // *TaskTracker должен предоставлять возможность удалять задачи по id или названию.
    public void deleteById(int id) {
        int i = 0;
        for (; i < count && tasks[i].getId()!=id; i++);
        moveLeftFromIndex(i);
    }

    public void deleteByName(String name) {
        int i = 0;
        for (; i < count && !tasks[i].getName().equals(name); i++);
        moveLeftFromIndex(i);
    }

    private void moveLeftFromIndex(int i) {
        if (i == count) {
            return;
        }
        for (; i < count-1; i++) {
            tasks[i] = tasks[i+1];
        }
        tasks[--count] = null;
    }

    public static void main(String[] args) {
        TaskTracker tt = new TaskTracker();

        tt.addTask("A", "A A", "A A", "", "A");
        tt.addTask("B", "B B", "B B", "", "A");
        tt.addTask("C", "C C", "A A", "", "A");
        tt.addTask("D", "A A", "D D", "", "A");
        tt.addTask("E", "A A", "A A", "", "A");
        tt.addTask("F", "A A", "A A", "", "A");
        tt.printAll();
        System.out.println("---");
        tt.deleteById(3);
        tt.printAll();
        System.out.println("---");
        tt.deleteByName("B");
        tt.printAll();
        System.out.println("---");
        tt.addTask("G", "A A", "A A", "", "A");
        tt.addTask("H", "B B", "B B", "", "A");
        tt.addTask("I", "C C", "A A", "", "A");
        tt.addTask("J", "A A", "D D", "", "A");
        tt.addTask("K", "A A", "A A", "", "A");
        tt.addTask("L", "A A", "A A", "", "A");
        tt.addTask("M", "A A", "A A", "", "A");
        tt.printAll();
        System.out.println("---");
    }
}
