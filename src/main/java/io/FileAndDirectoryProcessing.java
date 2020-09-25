package io;

import java.io.*;

public class FileAndDirectoryProcessing {
    // Напишите метод, который подсчитывает сколько раз в текстовом файле встречается
    // указанная последовательность символов с учетом регистра (только для латиницы)
    public static int countWordOccurrenceWithRegisterInFile(String fileName, String wordToCount) throws IOException {
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] wordsInLine = line.split("[^\\w']+");
                for (String wordInLine : wordsInLine) {
                    if (wordInLine.equals(wordToCount)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Напишите метод, который последовательно сшивает все текстовые файлы в указанном каталоге в один файл
    // (подкаталоги обходить не надо)
    public static int mergeFilesInDirectory(String directoryName, String outputFileName) throws IOException {
        int resultLineCount = 0;
        File directory = new File(directoryName);
        File[] files = directory.listFiles();

        if (files != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                for (File file : files) {
                    if (file.isFile()) {
                        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                            writer.write("//== from file '" + file + "'\n\n");
                            resultLineCount += 2;
                            String line;
                            while ((line = reader.readLine()) != null) {
                                writer.write(line + "\n");
                                resultLineCount++;
                            }
                            writer.write("\n//== end of file '" + file + "'\n\n\n");
                            resultLineCount += 4;
                        }
                    }
                }
            }
        }

        return resultLineCount + 1;
    }

    // Напишите метод, позволяющий удалить каталог с вложенными файлами и каталогами
    public static int deleteAllFilesInDirectoryRec(String directoryName) {
        int removedCount = 0;
        File directory = new File(directoryName);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                removedCount += deleteAllFilesInDirectoryRec(file);
                if (file.delete()) {
                    removedCount++;
                }
            }
        }

        return removedCount;
    }

    public static int deleteAllFilesInDirectoryRec(File file) {
        int removedCount = 0;

        if (file.isFile()) {
            if (file.delete()) {
                removedCount++;
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File embeddedFile : files) {
                    removedCount += deleteAllFilesInDirectoryRec(embeddedFile);
                    if (embeddedFile.delete()) {
                        removedCount++;
                    }
                }
            }
            if (file.delete()) {
                removedCount++;
            }
        }

        return removedCount;
    }
}
