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

    /**
     * Registering the directory in the watch service. It will be responsible for monitor the events inside the directory
     *
     * @param watchService
     * @param path
     * @param directory
     * @throws IOException
     */
    public void registerFolderInWatchService(WatchService watchService, Path path, String directory) throws IOException {
        WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        processFile(watchKey, directory);
    }

    /**
     * Start the process of the file.
     *
     * @param watchKey
     * @param directory
     * @throws IOException
     */
    private void processFile(WatchKey watchKey, String directory) throws IOException {
        while (true) {
            monitorEventsInDirectory(watchKey, directory);
            if (!watchKey.reset())
                break;
        }
    }

    /**
     * Check events in the directory. Every event will be caught here
     *
     * @param watchKey
     * @param directory
     * @throws IOException
     */
    private void monitorEventsInDirectory(WatchKey watchKey, String directory) throws IOException {
        for (WatchEvent<?> event : watchKey.pollEvents()) {
            this.setFileStatus(FileStatus.PROCESSING);
            WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
            Path fileName = pathEvent.context();
            boolean fileDoNotHaveInvalidName = DirectoryUtils.hasInvalidName(fileName);

            if (fileDoNotHaveInvalidName && !fileName.toString().equals("processed")) {
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

    /**
     * Set the statistic data for the current file that is being analyzed
     *
     * @param monitoredPath
     */
    private void buildStatitistics(Path monitoredPath) {
        Statistics statistics = new Statistics();
        statistics.extractStatisticsFromFile(statistics, monitoredPath);
        setStatistics(statistics);
    }

    /**
     * Move the processed files to processed directory
     *
     * @param directory
     * @param fileName
     * @throws IOException
     */
    private void moveToProcessedDirectory(String directory, String fileName) throws IOException {

        final String PROCESSED_FILES_DIRECTORY = directory + "/" + "processed";
        Path targetDirectory = Paths.get(PROCESSED_FILES_DIRECTORY);
        Path currentDirectoryFile = Paths.get(directory + "/" + fileName);
        Path targetDirectoryFile = Paths.get(PROCESSED_FILES_DIRECTORY + "/" + fileName);

        if (!targetDirectory.toFile().isDirectory()) {
            new File(PROCESSED_FILES_DIRECTORY).mkdirs();
            Files.move(currentDirectoryFile, targetDirectoryFile);
        } else {
            if (!Files.exists(targetDirectoryFile))
                Files.move(currentDirectoryFile, targetDirectoryFile);
        }
    }

    @Override
    public String toString() {
        return " ==== Statistics of file: '" + this.getFileName() + "' ==== \n" +
                " - File Status: " + this.getFileStatus() + "\n" +
                " - Amount of words in file: " + this.getStatistics().getNumberOfWordsInFile() + "\n" +
                " - Amount of dots in file: " + this.getStatistics().getNumberOfDotsInFile() + "\n" +
                " - Most frequent word in file: '" + this.getStatistics().getMostFrequentWordInFile() + "'\n" +
                "=========================================================== ";
    }
}
