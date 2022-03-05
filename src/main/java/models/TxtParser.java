package models;

import enums.FileStatus;
import exceptions.CustomFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TxtParser extends FileParser {

    public TxtParser() {
        this.setFileExtension(".txt");
    }

    @Override
    public FileParser getFileParser(String directory) {
        try {

            // Monitoring file
            WatchService watchService = FileSystems.getDefault().newWatchService();

            // Reading directory that we want to monitor
            Path monitoredPath = Path.of(directory);

            // Registering the folder in watch service. Here I want to monitor the CREATE, MODIFY AND DELETE event of the folder
            WatchKey watchKey = registerFolderInWatchService(watchService, monitoredPath);

            while (true) {
                this.setFileStatus(FileStatus.PROCESSING);
                checkEventsInDirectory(watchKey, directory);
                if (!watchKey.reset())
                    break;
            }

        } catch (IOException e) {
            this.setFileStatus(FileStatus.ERROR);
            e.printStackTrace();
        }
        return this;
    }

    private static WatchKey registerFolderInWatchService(WatchService watchService, Path path) {
        try {
            return path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            throw new CustomFileException(e.getMessage());
        }
    }

    private void checkEventsInDirectory(WatchKey watchKey, String directory) {
        for (WatchEvent<?> event : watchKey.pollEvents()) {

            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
            Path fileName = pathEvent.context();
            boolean fileDoNotHaveInvalidName = hasInvalidName(fileName);

            if (fileDoNotHaveInvalidName) {
                System.out.println("File Name: " + fileName);

                File file = new File(directory + "/" + fileName);
                Path monitoredPath = Path.of(directory + "/" + fileName);

                if (file.exists()) {
                    Statistics statistics = new Statistics();
                    var result = statistics.getNumberOfWordsInFile(monitoredPath);
                    long numberOfWordsInFile = result.values().stream().mapToInt(Long::intValue).sum();;
                    long numberOfDotsInFile = statistics.getNumberOfDotsInFile(monitoredPath);
                    String mostFrequentWordInFile = statistics.getMostFrequentWordInFile(result);
                    buildStatisticsOfFile(numberOfWordsInFile, numberOfDotsInFile, mostFrequentWordInFile);
                }
            }
        }
        this.setFileStatus(FileStatus.PROCESSED);
    }

    private static boolean hasInvalidName(Path fileName) {
        final String UBUNTU_TEMPORARY_FILE_CREATED_AUTOMATICALLY = ".goutputstream";
        return !fileName
                .getFileName()
                .toString()
                .toLowerCase()
                .startsWith(UBUNTU_TEMPORARY_FILE_CREATED_AUTOMATICALLY);
    }

    private void buildStatisticsOfFile(long numberOfWordsInFile, long numberOfDotsInFile, String mostFrequentWordInFile) {
        Statistics statistics = new Statistics();
        statistics.setNumberOfWordsInFile(numberOfWordsInFile);
        statistics.setNumberOfDotsInFile(numberOfDotsInFile);
        statistics.setMostFrequentWordInFile(mostFrequentWordInFile);
        this.setStatistics(statistics);
    }
}
