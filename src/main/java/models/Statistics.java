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

    public Statistics() {

    }

    public Statistics(long numberOfWordsInFile, long numberOfDotsInFile, String mostFrequentWordInFile) {
        this.numberOfWordsInFile = numberOfWordsInFile;
        this.numberOfDotsInFile = numberOfDotsInFile;
        this.mostFrequentWordInFile = mostFrequentWordInFile;
    }

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
        Map.Entry<String, Long> maxEntry = null;
        for (Map.Entry<String, Long> entry : result.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
    }

    public void extractStatisticsFromFile(Statistics statistics, Path monitoredPath) {
        var result = statistics.getFileInformation(monitoredPath);
        long numberOfWordsInFile = result.values().stream().mapToInt(Long::intValue).sum();
        long numberOfDotsInFile = statistics.getNumberOfDotsInFile(monitoredPath);
        String mostFrequentWordInFile = statistics.getMostFrequentWordInFile(result);
        setStatisticsInformation(statistics, numberOfWordsInFile, numberOfDotsInFile, mostFrequentWordInFile);
    }

    private void setStatisticsInformation(Statistics statistics, long numberOfWordsInFile, long numberOfDotsInFile, String mostFrequentWordInFile) {
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
