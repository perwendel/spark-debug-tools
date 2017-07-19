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
 * Assumes conventions are followed (e.g. path.to.Class -> src/main/java/path/to/Class.java).
 */
public class LocalSourceLocator implements SourceLocator {
  private final File basePathFile;

  /**
   * @param basePath The path of the directory to search for source files within.
   */
  public LocalSourceLocator(String basePath) {
    this.basePathFile = new File(basePath);
  }

  /**
   * @param basePath The directory to search for source files within.
   */
  public LocalSourceLocator(File basePath) {
    this.basePathFile = basePath;
  }

  @Override
  public String toString() {
    try {
      return this.basePathFile.getCanonicalPath();
    } catch (Exception e) {
      return this.basePathFile.getPath();
    }
  }

  @Override
  public Optional<SourceFile> findFileForFrame(StackTraceElement frame) {
    // Cannot find a file if the frame does not have one.
    if (frame.getFileName() == null) {
      return Optional.absent();
    }

    // Ignore cases where the directory doesn't exist/cannot be read.
    // No point in returning an error here since this is for a debug screen.
    if (!basePathFile.exists() || !basePathFile.isDirectory() || !basePathFile.canRead()) {
      return Optional.absent();
    }

    // This is where things get a little bit tricky.
    // The stack frame only contains the file name (e.g. "File.java"), but not the full path.
    // The compiled byte code also does not contain the full path.
    // Our strategy here is to find all files with a matching file name and pare them down.
    // We may not always find a file (e.g. sometimes they won't be available like in a JAR).

    Collection<File> possibilities = FileUtils.listFiles(basePathFile, new IOFileFilter() {
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
      // Assume the matched file is the source file (we may be wrong, but we can't know).
      return Optional.of(new LocalSourceFile(Iterables.first(possibilities)));
    }

    if (possibilities.size() > 1) {
      // Use the canonical class name to identify the correct file.
      // Assumes the directory structure matches the package and name of the class.
      // e.g. edu.rice.mschurr.Hello -> src/main/java/edu/rice/mschurr/Hello.java
      String className = frame.getClassName();

      // Remove anything after the $ in the class name to get the declaring class name.
      // Effectively, this finds the top-level class name for lambdas and nested classes.
      if (className.indexOf('$') != -1) {
        className = className.substring(0, className.lastIndexOf('$'));
      }

      // Build the expected path of the file.
      String path = File.separatorChar + className.replace('.', File.separatorChar) + ".java";

      // Check each possibility against the expected path.
      try {
        for (File file : possibilities) {
          if (file.getCanonicalPath().endsWith(path)) {
            return Optional.of(new LocalSourceFile(file));
          }
        }
      } catch (IOException e) {
        // Ignore (the provided frame just won't have a code snippet).
      }
    }

    return Optional.absent();
  }
}
