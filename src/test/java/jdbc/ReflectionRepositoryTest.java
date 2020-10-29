package jdbc;

public class ReflectionRepositoryTest {
    public static void main(String[] args) {
        try (JDBC jdbc = new JDBC()) {

            // 1st test. Class with id, insert object with id
            ReflectionRepository<TestClassWithId> repWithId = new ReflectionRepository<>(jdbc, TestClassWithId.class);
            repWithId.printAll();
            jdbc.getMetadata();

            TestClassWithId test1 = new TestClassWithId(3L, "test", 16);
            repWithId.addObject(test1);

            TestClassWithId test2 = repWithId.getObject(3L);
            if (test1.equals(test2)) {
                System.out.println("== Test #1 OK ==");
            } else {
                System.out.println("== Test #1 FAILED ==");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
