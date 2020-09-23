package refliction_api;

public class TestClass {
    @Test
    static void testStatic() {
        System.out.println("Test Static");
    }

    @Test
    void testDef() {
        System.out.println("Test Default");
    }

    @Test(prior = 1)
    void test1() {
        System.out.println("Test 1");
    }

    @Test(prior = 2)
    void test2() {
        System.out.println("Test 2");
    }

//== Runtime Exception 👍
//    @Test(prior = 0)
//    void test0() {
//        System.out.println("Test 0");
//    }

//== Runtime Exception 👍
//    @Test(prior = 11)
//    void test11() {
//        System.out.println("Test 11");
//    }

//== Runtime Exception 👍
//    @Test
//    private void testInt(int value) {
//        System.out.println("Test Int " + value);
//    }

    @Test(prior = 2)
    void test22() {
        System.out.println("Test 22");
    }

    @Test
    private void testPrivate() {
        System.out.println("Test Private");
    }

    @BeforeSuite
    void beforeSuite() {
        System.out.println("Before Suite");
    }

    @AfterSuite
    void afterSuite() {
        System.out.println("After Suite");
    }


//== Runtime Exception 👍
//    @BeforeSuite
//    void beforeSuite2() {
//        System.out.println("Before Suite 2");
//    }

//== Runtime Exception 👍
//    @AfterSuite
//    void afterSuite2() {
//        System.out.println("After Suite 2");
//    }

    void notTest() {
        System.out.println("Not a test");
    }

}
