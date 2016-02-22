package spark.debug;

import java.io.File;

import com.google.common.base.Optional;

/**
 * Defines an interface for locating source files.
 *
 * @author mschurr
 */
public interface SourceLocator {
    /**
     * @param frame A stack trace frame.
     * @return A file containing the code referenced by the StackTraceElement.
     */
    public Optional<File> findFileForFrame(StackTraceElement frame);
}
