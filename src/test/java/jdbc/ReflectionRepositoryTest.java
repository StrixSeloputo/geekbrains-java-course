package jdbc;

import java.sql.SQLException;

public class ReflectionRepositoryTest {
    public static void main(String[] args) {
        try {
            ReflectionRepository<TestClassWithId> repWithId = new ReflectionRepository<>(TestClassWithId.class);
            repWithId.printAll();
            JDBC.getMetadata();

            TestClassWithId test1 = new TestClassWithId(3L, "test", 16);
            repWithId.addObject(test1);
            repWithId.printAll();

            TestClassWithId test2 = repWithId.getObject(3L);
            repWithId.printAll();
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }
}
