package spark.debug;

import static spark.Spark.exception;
import static spark.Spark.modelAndView;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Renders an interactive in-browser stack trace when a controller throws an uncaught exception.
 */
public class DebugScreen implements ExceptionHandler<Exception> {
  protected final FreeMarkerEngine templateEngine;
  protected final Configuration templateConfig;
  protected final SourceLocator[] sourceLocators;

  public DebugScreen() {
    this(new LocalSourceLocator("./src/main/java"),
         new LocalSourceLocator("./src/test/java"));
  }

  public DebugScreen(SourceLocator... sourceLocators) {
    templateConfig = new Configuration(new Version(2, 3, 23));

    if (isDevelopmentMode()) {
      try {
        templateConfig.setDirectoryForTemplateLoading(new File("./src/main/resources"));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } else {
      templateConfig.setClassForTemplateLoading(getClass(), "/");
    }

    templateEngine = new FreeMarkerEngine(templateConfig);
    this.sourceLocators = sourceLocators;
  }

  private static boolean isDevelopmentMode() {
    return "true".equals(System.getenv("SPARK_DEBUG_SCREEN_DEV"));
  }

  /**
   * Enables the debug screen to catch any exception (Exception.class).
   * Assumes code is located in src/main/java (relative to working directory).
   * See README.md for advanced usage instructions.
   * @throws Exception
   */
  public static void enableDebugScreen() {
      exception(Exception.class, new DebugScreen());
  }

  /**
   * Enables the debug screen to catch any exception (Exception.class).
   * Finds code using the provided source locators.
   * @param sourceLocators Used to find source files (in priority order).
   */
  public static void enableDebugScreen(SourceLocator... sourceLocators) {
      exception(Exception.class, new DebugScreen(sourceLocators));
  }

  @Override
  public void handle(Exception exception, Request request, Response response) {
    handleThrowable(exception, request, response);
  }

  private void addToChain(Throwable throwable,
                          ArrayList<LinkedHashMap<String, Object>> exceptionChain,
                          boolean isSuppressed) {
    LinkedHashMap<String, Object> exceptionInfo = new LinkedHashMap<>();
    String trace = traceToString(throwable);
    exceptionInfo.put("frames", parseFrames(throwable));
    exceptionInfo.put("short_message", StringUtils.abbreviate(Optional.fromNullable(throwable.getMessage()).or(""), 100));
    exceptionInfo.put("full_trace", trace);
    exceptionInfo.put("show_full_trace", trace.length() > 100);
    exceptionInfo.put("message", Optional.fromNullable(throwable.getMessage()).or(""));
    exceptionInfo.put("plain_exception", ExceptionUtils.getStackTrace(throwable));
    exceptionInfo.put("name", throwable.getClass().getCanonicalName().split("\\."));
    exceptionInfo.put("basic_type", throwable.getClass().getSimpleName());
    exceptionInfo.put("type", throwable.getClass().getCanonicalName());
    exceptionInfo.put("suppressed", isSuppressed);
    exceptionChain.add(exceptionInfo);
  }

  private void crawlChain(Throwable throwable,
                          ArrayList<LinkedHashMap<String, Object>> exceptionChain,
                          boolean isSuppressed) {
    addToChain(throwable, exceptionChain, isSuppressed);

    for (Throwable suppressedThrowable : throwable.getSuppressed()) {
      crawlChain(suppressedThrowable, exceptionChain, true);
    }

    if (throwable.getCause() != null) {
      crawlChain(throwable.getCause(), exceptionChain, false);
    }
  }

  public final void handleThrowable(Throwable throwable,
                                    Request request,
                                    Response response) {
    // Set the response status (important so that AJAX requests will fail).
    response.status(500);

    try {
      LinkedHashMap<String, Object> model = new LinkedHashMap<>();
      ArrayList<LinkedHashMap<String, Object>> exceptionChain = new ArrayList<>();
      crawlChain(throwable, exceptionChain, false);
      model.put("exceptions", exceptionChain);

      LinkedHashMap<String, Map<String, ? extends Object>> tables = new LinkedHashMap<>();
      installTables(tables, request, throwable);
      model.put("tables", tables);

      response.type("text/html; charset=UTF-8");
      response.body(templateEngine.render(modelAndView(model, "debugscreen.ftl")));
    } catch (Exception e) {
      // A simple fall-back (in case an error occurs trying to generate the debug screen itself).
      response.body(
                "<html>"
              + "  <head>"
              + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
              + "  </head>"
              + "  <body>"
              + "    <h1>Caught Exception:</h1>"
              + "    <pre>"
              +        ExceptionUtils.getStackTrace(throwable)
              + "    </pre>"
              + "    <h1>Caught Exception (while rendering debug screen):</h1>"
              + "    <pre>"
              +        ExceptionUtils.getStackTrace(e)
              + "    </pre>"
              + "  </body>"
              + "</html>");
    }
  }

  protected void installTables(LinkedHashMap<String, Map<String, ? extends Object>> tables,
                               Request request,
                               Throwable throwable) {
    tables.put("Request Headers", setToLinkedHashMap(request.headers(), h -> h, request::headers));
    tables.put("Request Properties", getRequestInfo(request));
    tables.put("Route Parameters", request.params());
    tables.put("Query Parameters", setToLinkedHashMap(request.queryParams(), p -> p, request::queryParams));
    tables.put("Session Attributes", setToLinkedHashMap(request.session().attributes(), a -> a, request.session()::attribute));
    tables.put("Request Attributes", setToLinkedHashMap(request.attributes(), a -> a, request::attribute));
    tables.put("Cookies", request.cookies());
    tables.put("Environment", getEnvironmentInfo());
    tables.put("Environment Variables", System.getenv());
  }

  private LinkedHashMap<String, Object> getEnvironmentInfo() {
      LinkedHashMap<String, Object> environment = new LinkedHashMap<>();
      environment.put("Thread ID", Thread.currentThread().getId());
      return environment;
  }

  private LinkedHashMap<String, Object> getRequestInfo(Request request) {
      LinkedHashMap<String, Object> req = new LinkedHashMap<>();
      req.put("URL", Optional.fromNullable(request.url()).or("-"));
      req.put("Scheme", Optional.fromNullable(request.scheme()).or("-"));
      req.put("Method", Optional.fromNullable(request.requestMethod()).or("-"));
      req.put("Protocol", Optional.fromNullable(request.protocol()).or("-"));
      req.put("Remote IP", Optional.fromNullable(request.ip()).or("-"));
      return req;
  }

  private LinkedHashMap<String, String> setToLinkedHashMap(Set<String> set,
                                                           Function<String, String> keyMapper,
                                                           Function<String, String> valueMapper) {
    return set.stream().collect(Collectors.toMap(keyMapper,
                                                 valueMapper,
                                                 (k, v) -> k,
                                                 LinkedHashMap::new));
  }

  private String traceToString(Throwable t) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    if (t instanceof TemplateException) {
      ((TemplateException) t).printStackTrace(pw, false, true, false);
    } else {
      t.printStackTrace(pw);
    }
    pw.close();
    return sw.toString();
  }

  /**
   * Parses all stack frames for an exception into a view model.
   *
   * @param e An exception.
   * @return A view model for the frames in the exception.
   */
  private List<Map<String, Object>> parseFrames(Throwable e) {
    ImmutableList.Builder<Map<String, Object>> frames = ImmutableList.builder();

    for (StackTraceElement frame : e.getStackTrace()) {
      frames.add(parseFrame(frame));
    }

    return frames.build();
  }

  /**
   * Parses a stack frame into a view model.
   *
   * @param sframe A stack trace frame.
   * @return A view model for the given frame in the template.
   */
  private Map<String, Object> parseFrame(StackTraceElement sframe) {
    ImmutableMap.Builder<String, Object> frame = ImmutableMap.builder();
    frame.put("file", Optional.fromNullable(sframe.getFileName()).or("<#unknown>"));
    frame.put("class", Optional.fromNullable(sframe.getClassName()).or(""));
    frame.put("line", Optional.fromNullable(Integer.toString(sframe.getLineNumber())).or(""));
    frame.put("function", Optional.fromNullable(sframe.getMethodName()).or(""));
    frame.put("comments", ImmutableList.of());

    // Try to find the source file corresponding to this exception stack frame.
    // Go through the locators in order until the source file is found.
    Optional<SourceFile> file = Optional.absent();
    for (SourceLocator locator : sourceLocators) {
      file = locator.findFileForFrame(sframe);

      if (file.isPresent()) {
        break;
      }
    }

    if (file.isPresent()) {
      // Fetch +-10 lines from the triggering line.
      Optional<Map<Integer, String>> codeLines = file.get().getLines(sframe);

      if (codeLines.isPresent()) {
        // Write the starting line number (1-indexed).
        frame.put("code_start",
                  Iterables.reduce(codeLines.get().keySet(), Integer.MAX_VALUE, Math::min) + 1);

        // Write the code as a single string, replacing empty lines with a " ".
        frame.put("code",
                  Joiner.on("\n").join(Iterables.map(codeLines.get().values(),
                                                     (x) -> x.length() == 0 ? " " : x)));

        // Write the canonical path.
        try {
          frame.put("canonical_path", file.get().getPath());
        } catch (Exception e) {
          // Not much we can do, so ignore and just don't have the canonical path.
        }
      }
    }

    return frame.build();
  }
}
