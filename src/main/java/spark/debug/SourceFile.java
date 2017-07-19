package spark.debug;

import java.util.Map;

import com.google.common.base.Optional;

public interface SourceFile {
  /**
   * @return The path of the file (to display)
   */
  public String getPath();

  /**
   * @param frame A stack frame.
   * @return Returns +- 10 lines of the file's content (centered on the line in the stack frame). A
   *         map of line numbers in the file to the contents of that line.
   */
  public Optional<Map<Integer, String>> getLines(StackTraceElement frame);
}
