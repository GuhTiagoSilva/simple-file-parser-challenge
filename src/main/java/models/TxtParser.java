package models;

import enums.FileStatus;
import utils.DirectoryUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;

public class TxtParser extends FileParser {

    @Override
    public FileParser analyzeFile(String directory) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path monitoredDirectory = Path.of(directory);
            DirectoryUtils.checkIfDirectoryExists(monitoredDirectory);
            registerFolderInWatchService(watchService, monitoredDirectory, directory);
        } catch (IOException e) {
            this.setFileStatus(FileStatus.ERROR);
            e.printStackTrace();
        }
        return this;
    }
}
