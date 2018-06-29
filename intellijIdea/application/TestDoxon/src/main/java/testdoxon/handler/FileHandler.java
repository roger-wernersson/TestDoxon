package testdoxon.handler;

import testdoxon.exceptionHandler.TDException;
import testdoxon.model.TDTableItem;
import testdoxon.repository.FileRepository;

import java.io.File;

public class FileHandler {
    private FileRepository fileRepository;

    /**
     * constructor
     */
    public FileHandler() {
        this.fileRepository = new FileRepository();
    }

    /**
     * @param filePath
     * @return String []
     * @throws TDException
     */
    public TDTableItem[] getMethodsFromFile(String filePath) throws TDException {
        if (this.fileExists(filePath)) {
            return fileRepository.fetchMethodNames(filePath);
        } else {
            throw new TDException(TDException.FILE_NOT_FOUND);
        }
    }

    /**
     * Checks if filePath is valid or not
     * Returns false when filePath is null or empty
     *
     * @param filePath
     * @return boolean
     */
    public boolean fileExists(String filePath) {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        return file.isFile();
    }

    /**
     * Return 0 if filePath or methodName is null or empty
     *
     * @param filePath
     * @param methodName
     * @return int
     * @throws TDException
     */
    public int getLineNumberOfSpecificMethod(String filePath, String methodName) throws TDException {
        if (filePath == null || methodName == null || filePath.isEmpty() || methodName.isEmpty()) {
            return 0;
        }

        return fileRepository.findLineNumberOfMethod(filePath, methodName);
    }
}
