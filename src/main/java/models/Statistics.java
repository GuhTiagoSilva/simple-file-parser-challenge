package models;

import exceptions.CustomFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Statistics {

    private long numberOfWordsInFile;
    private long numberOfDotsInFile;
    private String mostFrequentWordInFile;

    public long getNumberOfWordsInFile() {
        return numberOfWordsInFile;
    }

    public void setNumberOfWordsInFile(long numberOfWordsInFile) {
        this.numberOfWordsInFile = numberOfWordsInFile;
    }

    public long getNumberOfDotsInFile() {
        return numberOfDotsInFile;
    }

    public void setNumberOfDotsInFile(long numberOfDotsInFile) {
        this.numberOfDotsInFile = numberOfDotsInFile;
    }

    public String getMostFrequentWordInFile() {
        return mostFrequentWordInFile;
    }

    public void setMostFrequentWordInFile(String mostFrequentWordInFile) {
        this.mostFrequentWordInFile = mostFrequentWordInFile;
    }

    /**
     * Get a map containing the words of the file as the key and the amount of times they repeat as the value
     * @param monitoredPath
     * @return
     */
    public Map<String, Long> getFileInformation(Path monitoredPath) {
        Map<String, Long> result;
        try {
            result = Files.lines(monitoredPath)
                    .flatMap(line -> Stream.of(line.split(" ")))
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
        } catch (IOException e) {
            throw new CustomFileException("An error occurred during the process of get file information");
        }

        result.entrySet().removeIf(entry -> entry.getKey().equals(""));
        return result;
    }


    public Long getNumberOfDotsInFile(Path monitoredPath) {
        try {
            long numberOfDots = Files.lines(monitoredPath)
                    .filter(x -> x.contains("."))
                    .collect(Collectors.toList()).size();
            return numberOfDots;
        } catch (IOException e) {
            throw new CustomFileException("An error occurred during the process of get number of dots in file");
        }
    }

    public String getMostFrequentWordInFile(Map<String, Long> result) {
        Map.Entry<String, Long> mostFrequentWord = null;
        for (Map.Entry<String, Long> fileWord : result.entrySet()) {
            if (mostFrequentWord == null || fileWord.getValue() > mostFrequentWord.getValue()) {
                mostFrequentWord = fileWord;
            }
        }
        return mostFrequentWord.getKey();
    }

    /**
     * Extract the statiscts created and set in statistics object
     * @param statistics
     * @param monitoredPath
     */
    public void extractStatisticsFromFile(Statistics statistics, Path monitoredPath) {
        var result = statistics.getFileInformation(monitoredPath);
        long numberOfWordsInFile = result.values().stream().mapToInt(Long::intValue).sum();
        long numberOfDotsInFile = statistics.getNumberOfDotsInFile(monitoredPath);
        String mostFrequentWordInFile = statistics.getMostFrequentWordInFile(result);
        setStatisticsInformation(statistics, numberOfWordsInFile, numberOfDotsInFile, mostFrequentWordInFile);
    }

    protected void setStatisticsInformation(Statistics statistics, long numberOfWordsInFile, long numberOfDotsInFile, String mostFrequentWordInFile) {
        statistics.setNumberOfWordsInFile(numberOfWordsInFile);
        statistics.setNumberOfDotsInFile(numberOfDotsInFile);
        statistics.setMostFrequentWordInFile(mostFrequentWordInFile);
    }

    @Override
    public String toString() {
        return " ==== Statistics of file ==== \n" +
                " - Amount of words in file: " + numberOfWordsInFile + "\n" +
                " - Amount of dots in file: " + numberOfDotsInFile + "\n" +
                " - Most frequent word in file: '" + mostFrequentWordInFile + "'\n" +
                "============================= ";
    }
}
