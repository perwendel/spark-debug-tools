package spark.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

/**
 * A source file located on a local file system.
 */
public class LocalSourceFile implements SourceFile {
  private final File file;

  public LocalSourceFile(File file) {
    this.file = file;
  }

  @Override
  public String getPath() {
    return file.getPath();
  }

  @Override
  public Optional<Map<Integer, String>> getLines(StackTraceElement frame) {
    // If the frame has no line number attached, we can't fetch anything.
    if (frame.getLineNumber() < 0) {
      return Optional.absent();
    }

    // Otherwise, fetch 20 lines centered on the number provided in the trace.
    ImmutableMap.Builder<Integer, String> lines = ImmutableMap.builder();
    int start = Math.max(frame.getLineNumber() - 10, 0);
    int end = start + 20;
    int current = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (current < start) {
          current++;
          continue;
        }

        if (current > end) {
          break;
        }

        lines.put(current, line);
        current++;
      }

      return Optional.of(lines.build());
    } catch (Exception e) {
      // Ignore and move on (the frame just won't have a code snippet).
      return Optional.absent();
    }
  }
}
