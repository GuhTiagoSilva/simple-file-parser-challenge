package models;

import enums.FileStatus;

public abstract class FileParser {

    private String fileExtension;
    private FileStatus fileStatus;
    private Statistics statistics;

    public abstract FileParser getFileParser(String directory);

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
