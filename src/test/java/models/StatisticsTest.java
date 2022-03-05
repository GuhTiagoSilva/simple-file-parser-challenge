package models;

import exceptions.CustomFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StatisticsTest {

    @TempDir
    Path temporaryDirectory;

    private Path invalidPath;
    private Path validPath;

    private File validFile;
    private File invalidFile;

    private Statistics statistics;
    private Map<String, Long> mostFrequenWordsInFile;

    @BeforeEach
    void setup() throws Exception {

        invalidPath = temporaryDirectory.resolve("test");
        validPath = temporaryDirectory.resolve("test1");

        validFile = temporaryDirectory.toFile();
        invalidFile = temporaryDirectory.toFile();

        statistics = buildStatistics();
        mostFrequenWordsInFile = new HashMap<>();

        mostFrequenWordsInFile.put("hello", 2L);
        mostFrequenWordsInFile.put("of", 60L);
    }

    @Test
    public void getMostFrequentWordInFileShouldReturnTheMostFrequentWordInAFile() {
        String mostFrequentWord = statistics.getMostFrequentWordInFile(mostFrequenWordsInFile);
        Assertions.assertNotNull(mostFrequentWord);
        Assertions.assertTrue(!mostFrequentWord.isEmpty());
        Assertions.assertEquals(mostFrequentWord, "of");
    }

    @Test
    public void getFileInformationShouldThrowCustomFileExceptionWhenInvalidPath() {
        Assertions.assertThrows(CustomFileException.class, () -> {
           statistics.getFileInformation(invalidPath);
        });
    }

    @Test
    public void getMostFrequentWordInFileShouldReturnMostFrequentWordsInFile() {
        String mostFrequentWordsInFile = statistics.getMostFrequentWordInFile();
        Assertions.assertNotNull(mostFrequentWordsInFile);
        Assertions.assertFalse(mostFrequentWordsInFile.isEmpty());
    }

    @Test
    public void getNumberOfDotsInFileShouldReturnNumberOfDotsInFile() {
        long numberOfDotsInFile = statistics.getNumberOfDotsInFile();
        Assertions.assertTrue(numberOfDotsInFile > 0);
    }

    @Test
    public void getNumberOfWordsInFileShouldReturnNumberOfWordsInFile() {
        long numberOfWordsInFile = statistics.getNumberOfWordsInFile();
        Assertions.assertTrue(numberOfWordsInFile > 0);
    }

    @Test
    public void setStatisticsInformationShouldSetDataInStatisticsObject() {
        statistics.setStatisticsInformation(statistics, 10L, 2L, "hello");
        Assertions.assertEquals(statistics.getMostFrequentWordInFile(), "hello");
        Assertions.assertEquals(statistics.getNumberOfWordsInFile(), 10L);
        Assertions.assertEquals(statistics.getNumberOfDotsInFile(), 2L);
    }

    private Statistics buildStatistics() {
        Statistics statistics = new Statistics();
        statistics.setNumberOfDotsInFile(30);
        statistics.setNumberOfWordsInFile(40);
        statistics.setMostFrequentWordInFile("of");
        return statistics;
    }
}
