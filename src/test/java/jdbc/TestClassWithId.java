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

    TestClassWithId(String str, Integer integer) {
        this.str = str;
        this.integer = integer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof TestClassWithId) {
            TestClassWithId casted = (TestClassWithId) obj;
            return this.id == casted.id && this.str.equals(casted.str) && this.integer.equals(casted.integer);
        }

        return false;
    }
}
