package exceptions;

public class MyArraySizeException extends IllegalArgumentException {
    public MyArraySizeException() {
        super("Ожидается матрица размером 4х4");
    }

}
