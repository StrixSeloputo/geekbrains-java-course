package oop.base;

public class Task {
    // Создайте класс Task (задача).
    // У задачи должны быть следующие свойства: id, название, имя владельца задачи, имя исполнителя, описание, статус.
    private static int countAll;
    private final int id;
    private final String name;
    private final String reporter;
    private String assignee;
    private String description;
    private String status;

    public Task(String name, String reporter, String assignee, String description, String status) {
        this.id = ++countAll;
        this.name = name;
        this.reporter = reporter;
        this.assignee = (assignee == null || assignee.equals("")) ? "Unassigned" : assignee;
        this.description = (description != null) ? description : "";
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Task-#"+id+"\t"+name+" are from "+reporter+" on "+assignee+
                " in status "+status+" and with description: \""+description+"\"";
    }
}
