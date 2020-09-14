package exceptions;

public class MyArrayDataException extends NumberFormatException {
    public MyArrayDataException(int i, int j, String elem) {
        super("Матричный элемент (" + i + "," + j + ") равен \"" + elem + "\" " +
                "и не удовлеворяет условию целочисленности элементов матрицы");
    }
}
