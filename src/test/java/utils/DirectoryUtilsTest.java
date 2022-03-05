package utils;

import exceptions.CustomFileException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

public class DirectoryUtilsTest {

    @TempDir
    Path temporaryDirectory;

    private String invalidDirectory;
    private Path pathWithInvalidNameOfFile;
    private Path pathWithValidNameOfFile;
    private Path invalidPathDirectory;
    private String invalidName;
    private String validFileName;

    @BeforeEach
    void setup() throws Exception {

        invalidName = ".goutputstream";
        validFileName = "test1";

        invalidDirectory = "invalidDirectoryTest";
        invalidPathDirectory = Path.of(invalidDirectory);

        pathWithInvalidNameOfFile = temporaryDirectory.resolve(invalidName);
        pathWithValidNameOfFile = temporaryDirectory.resolve(validFileName);
    }

    @Test
    public void checkIfDirectoryExistsShouldThrowCustomFileExceptionWhenDirectoryNotExists() {
        Assertions.assertThrows(CustomFileException.class, () -> {
            DirectoryUtils.checkIfDirectoryExists(invalidPathDirectory);
        });
    }

    @Test
    public void hasInvalidNameShouldReturnFalseIfTheFileNameStartsWithIncorrectName() {
        boolean haveValidName = DirectoryUtils.hasInvalidName(pathWithInvalidNameOfFile);
        Assertions.assertFalse(haveValidName);
    }

    @Test
    public void hasInvalidNameShouldReturnTrueIfTheFileNameDoesNotStartsWithIncorrectName() {
        boolean haveValidName = DirectoryUtils.hasInvalidName(pathWithValidNameOfFile);
        Assertions.assertTrue(haveValidName);
    }
}
