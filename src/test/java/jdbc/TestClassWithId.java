package jdbc;

@DbTable(name = "table_with_id")
public class TestClassWithId {
    @DbId
    private long id;

    @DbColumn
    private String str;

    @DbColumn
    private Integer integer;

    public TestClassWithId() {
    }

    TestClassWithId(long id, String str, Integer integer) {
        this.id = id;
        this.str = str;
        this.integer = integer;
    }
}
