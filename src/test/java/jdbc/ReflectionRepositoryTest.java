package jdbc;

public class ReflectionRepositoryTest {
    public static void main(String[] args) {
        try (JDBC jdbc = new JDBC()) {

            // 1st test. Class with id, insert object with id and get object exact this id
            ReflectionRepository<TestClassWithId> repWithId = new ReflectionRepository<>(jdbc, TestClassWithId.class);
            repWithId.printAll();
            jdbc.getMetadata();

            TestClassWithId test1 = new TestClassWithId(3L, "test", 16);
            repWithId.addObject(test1);

            TestClassWithId test2 = repWithId.getObject(3L);
            if (test1.equals(test2)) {
                System.out.println("== Test #1/2 OK ==");
            } else {
                System.out.println("== Test #1/2 FAILED ==");
            }


            // 2nd test. Class with id, insert object without id, get this id while inserting and get this object
            TestClassWithId test3 = new TestClassWithId("test", 16);
            long insertedId = repWithId.addObject(test3);

            TestClassWithId test4 = repWithId.getObject(insertedId);
            if (test4.equals(test3)) {
                System.out.println("== Test #3/4 OK ==");
            } else {
                System.out.println("== Test #3/4 FAILED ==");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
