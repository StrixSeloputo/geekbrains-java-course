package exceptions;

public class TotalizerOf4x4IntegerMatrix {
    // Напишите метод, на вход которого подаётся двумерный строковый массив размером 4х4. 
    // При подаче массива другого размера необходимо бросить исключение MyArraySizeException.

    // Далее метод должен пройтись по всем элементам массива, преобразовать в int и просуммировать. 
    // Если в каком-то элементе массива преобразование не удалось 
    // (например, в ячейке лежит символ или текст вместо числа), 
    // должно быть брошено исключение MyArrayDataException с детализацией, в какой именно ячейке лежат неверные данные.
    public static int sum(String[][] matrix) throws MyArraySizeException {
        if (matrix.length != 4) {
            throw new MyArraySizeException();
        }
        int sum = 0;
        for (String[] dim : matrix) {
            if (dim.length != 4) {
                throw new MyArraySizeException();
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                try {
                    int intElem = Integer.parseInt(matrix[i][j]);
                    sum += intElem;
                } catch (NumberFormatException ex) {
                    throw new MyArrayDataException(i, j, matrix[i][j]);
                }
            }
        }

        return sum;
    }

    // В методе main() вызвать полученный метод, 
    // обработать возможные исключения MyArraySizeException и MyArrayDataException и вывести результат расчета.
    public static void main(String[] args) {
        try {
            String[][] a = {{"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}};
            System.out.println(TotalizerOf4x4IntegerMatrix.sum(a));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            String[][] b = {{"1", "2", "3", "4", "5"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}};
            System.out.println(TotalizerOf4x4IntegerMatrix.sum(b));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            String[][] c = {{"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "xxx"}};
            System.out.println(TotalizerOf4x4IntegerMatrix.sum(c));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            String[][] d = {{"1", "2", "3", "4"}, {"1", "2", "3.", "4"}, {"1", "2", "3", "4"}, {"1", "2", "3", "4"}};
            System.out.println(TotalizerOf4x4IntegerMatrix.sum(d));
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
