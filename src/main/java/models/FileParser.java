package models;

import enums.FileStatus;
import utils.DirectoryUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public abstract class FileParser {

    private String fileName;
    private FileStatus fileStatus;
    private Statistics statistics;

    public abstract FileParser analyzeFile(String directory);

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void registerFolderInWatchService(WatchService watchService, Path path, String directory) throws IOException {
        WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        processFile(watchKey, directory);
    }

    private void processFile(WatchKey watchKey, String directory) throws IOException {
        while (true) {
            monitorEventsInDirectory(watchKey, directory);
            if (!watchKey.reset())
                break;
        }
    }

    private void monitorEventsInDirectory(WatchKey watchKey, String directory) throws IOException {
        for (WatchEvent<?> event : watchKey.pollEvents()) {

            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
            Path fileName = pathEvent.context();
            boolean fileDoNotHaveInvalidName = DirectoryUtils.hasInvalidName(fileName);

            if (fileDoNotHaveInvalidName) {
                final String PATH_TO_FILE = directory + "/" + fileName;
                File file = new File(PATH_TO_FILE);
                Path monitoredPath = Path.of(PATH_TO_FILE);

                if (file.exists()) {
                    buildStatitistics(monitoredPath);
                    this.setFileStatus(FileStatus.PROCESSED);
                    setFileName(fileName.toString());
                    System.out.println(this);
                    moveToProcessedDirectory(directory, fileName.toString());
                }
            }
        }
    }

    private void buildStatitistics(Path monitoredPath) {
        Statistics statistics = new Statistics();
        statistics.extractStatisticsFromFile(statistics, monitoredPath);
        setStatistics(statistics);
    }

    private void moveToProcessedDirectory(String directory, String fileName) throws IOException {

        final String PROCESSED_FILES_DIRECTORY = directory + "/" + "processed";

        if (!Paths.get(PROCESSED_FILES_DIRECTORY).toFile().isDirectory())
            new File(PROCESSED_FILES_DIRECTORY).mkdirs();
        Files.move(Paths.get(directory + "/" + fileName), Paths.get(PROCESSED_FILES_DIRECTORY + "/" + fileName));
    }

    @Override
    public String toString() {
        return " ==== Statistics of file: '" + this.getFileName() + "' ==== \n" +
                " - File Status: " + this.getFileStatus() + "\n" +
                " - Amount of words in file: " + this.getStatistics().getNumberOfWordsInFile() + "\n" +
                " - Amount of dots in file: " + this.getStatistics().getNumberOfDotsInFile() + "\n" +
                " - Most frequent word in file: '" + this.getStatistics().getMostFrequentWordInFile() + "\n" +
                "=========================================================== ";
    }
}
