package spark.debug;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.google.common.base.Optional;

/**
 * Locates source files by searching exhaustively within a base directory.
 * The base directory should contain all of the code for your app (e.g. src/main/java).
 *
 * @author mschurr
 */
public class FileSearchSourceLocator implements SourceLocator {
    private final File basePath;

    /**
     * @param basePath The directory to search for source files within (e.g. src/main/java).
     */
    public FileSearchSourceLocator(File basePath) {
        this.basePath = basePath;
    }

    @Override
    public Optional<File> findFileForFrame(StackTraceElement frame) {
        // If the frame has no file attached, we can't find a source file (obviously).
        if (frame.getFileName() == null) {
            return Optional.absent();
        }

        if (!basePath.exists() || !basePath.isDirectory() || !basePath.canRead()) {
            return Optional.absent();
        }

        // Find a list of all matching files (any files with the same name as the name provided
        // in the trace) within the base path.

        // We may not find a file in all cases because sometimes source files are just not
        // available (e.g. we only have .class files in compiled applications).

        // Since the stack trace only gives the file base name (and not the actual path),
        // we need to enumerate all possibilities.
        Collection<File> possibilities = FileUtils.listFiles(basePath, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().equals(frame.getFileName());
            }

            @Override
            public boolean accept(File dir, String name) {
                return name.equals(frame.getFileName());
            }
        }, TrueFileFilter.INSTANCE);

        if (possibilities.size() == 1) {
            // If there's only one possibility, assume it is the correct source file.
            File file = Iterables.first(possibilities);
            return Optional.of(file);
        } else if (possibilities.size() > 1) {
            // If there are multiple possibilities, use the class name to further filter down.
            // Assumes the directory structures matches the package name of the class.
            // e.g. edu.rice.mschurr.Hello -> src/main/java/edu/rice/mschurr/Hello.java
            String className = frame.getClassName();

            // Remove anything after the $ in the class name to get the base class.
            // Effectively, this finds the top-level class name for lambdas and nested classes.
            if (className.indexOf('$') != -1) {
                className = className.substring(0, className.lastIndexOf('$'));
            }

            // Build the effective path of the file (according to package and class name).
            // e.g. /path/to/package/Class.java
            String path = File.separatorChar + className.replace('.', File.separatorChar) + ".java";

            // Go through each possibility and see if it is in the desired package.
            try {
                for (File file : possibilities) {
                    if (file.getCanonicalPath().endsWith(path)) {
                        return Optional.of(file);
                    }
                }
            } catch (IOException e) {
                return Optional.absent();
            }

            return Optional.absent();
        } else {
            return Optional.absent();
        }
    }
}
