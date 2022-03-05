package utils;

import exceptions.CustomFileException;

import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryUtils {

    public static void checkIfDirectoryExists(Path directory) {
        if (!Files.exists(directory))
            throw new CustomFileException("Directory: " + directory + " does not exists. Please, review your input!");
    }

    public static boolean hasInvalidName(Path fileName) {
        final String UBUNTU_TEMPORARY_FILE_CREATED_AUTOMATICALLY = ".goutputstream";
        return !fileName
                .getFileName()
                .toString()
                .toLowerCase()
                .startsWith(UBUNTU_TEMPORARY_FILE_CREATED_AUTOMATICALLY);
    }


}
