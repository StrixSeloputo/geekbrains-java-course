package io;

import java.io.IOException;

public class FileAndDirectoryProcessingTest {

    public static void main(String[] args) {
        String currDir = null;
        try {
            currDir = new java.io.File("./src/test/java/io/test_resources").getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String fileName = currDir + "/count_test_file.txt";
            String word = "Meaning";
            int count = FileAndDirectoryProcessing.countWordOccurrenceWithRegisterInFile(fileName, word);
            System.out.println("In file '" + fileName + "' was founded " + count + " word(s) '" + word + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String mergeDir = currDir + "/merge_test_dir";
        try {
            String outputFileName = mergeDir + "/output.txt";
            int count = FileAndDirectoryProcessing.mergeFilesInDirectory(mergeDir, outputFileName);
            System.out.println("In directory '" + mergeDir + "' was created file '" + outputFileName +
                    "' and filled with " + count + " line(s) from existing in directory files");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String removeDir = currDir + "/remove_test_dir";
        int count = FileAndDirectoryProcessing.deleteAllFilesInDirectoryRec(removeDir);
        System.out.println("In directory '" + removeDir + "' was deleted " + count + " file(s) and/or directory(ies)");
    }
}
