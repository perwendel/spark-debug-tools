package spark.debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import static spark.Spark.exception;

public class DebugScreen implements ExceptionHandler {
    protected final static Logger logger = LoggerFactory.getLogger(DebugScreen.class);

    protected final FreeMarkerEngine templateEngine;
    protected final Configuration templateConfig;
    protected final ImmutableList<SourceLocator> sourceLocators;

    public DebugScreen() {
        templateEngine = new FreeMarkerEngine();
        templateConfig = new Configuration();
        templateConfig.setClassForTemplateLoading(getClass(), "/");
        templateEngine.setConfiguration(templateConfig);
        sourceLocators = ImmutableList.of(
                new FileSearchSourceLocator(new File("./src/main/java")),
                new FileSearchSourceLocator(new File("./src/test/java"))
        );
    }

    public DebugScreen(ImmutableList<SourceLocator> sourceLocators) {
        templateEngine = new FreeMarkerEngine();
        templateConfig = new Configuration();
        templateConfig.setClassForTemplateLoading(getClass(), "/");
        templateEngine.setConfiguration(templateConfig);
        this.sourceLocators = sourceLocators;
    }

    static void enableDebugScreen() {
        exception(Exception.class, new DebugScreen());
    }

    @Override
    public final void handle(Exception exception, Request request, Response response) {
        try {
            List<Map<String, Object>> frames = parseFrames(exception);

            LinkedHashMap<String, Object> model = new LinkedHashMap<>();
            model.put("message", Optional.fromNullable(exception.getMessage()).or(""));
            model.put("plain_exception", ExceptionUtils.getStackTrace(exception));
            model.put("frames", frames);
            model.put("name", exception.getClass().getCanonicalName().split("\\."));
            model.put("basic_type", exception.getClass().getSimpleName());
            model.put("type", exception.getClass().getCanonicalName());

            LinkedHashMap<String, Map<String, ? extends Object>> tables = new LinkedHashMap<>();
            installTables(tables, request, exception);
            model.put("tables", tables);

            response.body(templateEngine.render(Spark.modelAndView(model, "debugscreen.ftl")));
        } catch (Exception e) {
            // In case we encounter any exceptions trying to render the error page itself,
            // have this simple fallback.
            response.body(
                    "<html>"
                            + "  <body>"
                            + "    <h1>Caught Exception:</h1>"
                            + "    <pre>" + ExceptionUtils.getStackTrace(exception) + "</pre>"
                            + "    <h1>Caught Exception Rendering Whoops! Pretty Error Page:</h1>"
                            + "    <pre>" + ExceptionUtils.getStackTrace(e) + "</pre>"
                            + "  </body>"
                            + "</html>"
            );
        }
    }

    /**
     * Install any tables you want to be shown in environment details.
     *
     * @param tables
     */
    protected void installTables(LinkedHashMap<String, Map<String, ? extends Object>> tables,
                                 Request request,
                                 Exception exception) {

        LinkedHashMap<String, Object> req = new LinkedHashMap<>();
        tables.put("Request", req);
        req.put("User-Agent", Optional.fromNullable(request.userAgent()).or("-"));
        req.put("URI", Optional.fromNullable(request.uri()).or("-"));
        req.put("URL", Optional.fromNullable(request.url()).or("-"));
        req.put("Query String", Optional.fromNullable(request.queryString()).or("-"));
        req.put("Scheme", Optional.fromNullable(request.scheme()).or("-"));
        req.put("Method", Optional.fromNullable(request.requestMethod()).or("-"));
        req.put("Protocol", Optional.fromNullable(request.protocol()).or("-"));
        req.put("Port", Optional.fromNullable(Integer.toString(request.port())).or("-"));
        req.put("Path Info", Optional.fromNullable(request.pathInfo()).or("-"));
        req.put("Remote IP", Optional.fromNullable(request.ip()).or("-"));
        req.put("Host", Optional.fromNullable(request.host()).or("-"));
        req.put("Content Type", Optional.fromNullable(request.contentType()).or("-"));
        req.put("Content Length", request.contentLength() == -1 ? "-" : Integer.toString(request.contentLength()));
        req.put("Context Path", Optional.fromNullable(request.contextPath()).or("-"));
        req.put("Body", Optional.fromNullable(request.body()).or("-"));

        LinkedHashMap<String, Object> requestAttributes = new LinkedHashMap<>();
        tables.put("Request Attributes", requestAttributes);
        for (String attr : request.attributes()) {
            requestAttributes.put(attr, request.attribute(attr).toString());
        }

        tables.put("Route Parameters", request.params());

        LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();
        tables.put("Query Parameters", queryParams);
        for (String s : request.queryParams()) {
            queryParams.put(s, request.queryParams(s));
        }

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        tables.put("Request Headers", headers);
        for (String header : request.headers()) {
            if (!header.equals("Cookie")) {
                headers.put(header, request.headers(header));
            }
        }

        LinkedHashMap<String, Object> session = new LinkedHashMap<>();
        tables.put("Session", session);
        for (String s : request.session().attributes()) {
            session.put(s, request.session().attribute(s).toString());
        }

        tables.put("Cookies", request.cookies());

        LinkedHashMap<String, Object> environment = new LinkedHashMap<>();
        tables.put("Environment", environment);
        environment.put("Thread ID", Thread.currentThread().getId());
    }

    /**
     * Parses all stack frames for an exception into a view model.
     *
     * @param e An exception.
     * @return A view model for the frames in the exception.
     */
    private final List<Map<String, Object>> parseFrames(Exception e) {
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
    private final Map<String, Object> parseFrame(StackTraceElement sframe) {
        ImmutableMap.Builder<String, Object> frame = ImmutableMap.builder();
        frame.put("file", Optional.fromNullable(sframe.getFileName()).or("<#unknown>"));
        frame.put("class", Optional.fromNullable(sframe.getClassName()).or(""));
        frame.put("line", Optional.fromNullable(Integer.toString(sframe.getLineNumber())).or(""));
        frame.put("function", Optional.fromNullable(sframe.getMethodName()).or(""));
        frame.put("comments", ImmutableList.of());

        // Try to find the source file corresponding to this exception stack frame.
        // Go through the locators in order until the source file is found.
        Optional<File> file = Optional.absent();
        for (SourceLocator locator : sourceLocators) {
            file = locator.findFileForFrame(sframe);

            if (file.isPresent()) {
                break;
            }
        }

        // Fetch +-10 lines from the triggering line.
        Optional<Map<Integer, String>> codeLines = fetchFileLines(file, sframe);

        if (codeLines.isPresent()) {
            // Write the starting line number (1-indexed).
            frame.put("code_start",
                      Iterables.reduce(codeLines.get().keySet(), Integer.MAX_VALUE, (a, i) -> Math.min(a, i)) + 1);

            // Write the code as a single string, replacing empty lines with a " ".
            frame.put("code", Joiner.on("\n").join(Iterables.map(codeLines.get().values(), (x) ->
                    x.length() == 0 ? " " : x)));

            // Write the canonical path.
            try {
                frame.put("canonical_path", file.get().getPath());
            } catch (Exception e) {
                // Not much we can do, so ignore and just don't have the canonical path.
            }
        }

        return frame.build();
    }

    /**
     * Fetches the lines of the source file corresponding to a StackTraceElement (fetches 20 lines total
     * centered on the line number given in the trace).
     *
     * @param file  An optional text file.
     * @param frame A stack trace frame.
     * @return An optional map of line numbers to the content of the lines (not terminated with \n).
     */
    private final Optional<Map<Integer, String>> fetchFileLines(Optional<File> file, StackTraceElement frame) {
        // If no line number is given or no file exists, we can't fetch lines.
        if (!file.isPresent() || frame.getLineNumber() == -1) {
            return Optional.absent();
        }

        // Otherwise, fetch 20 lines centered on the number provided in the trace.
        ImmutableMap.Builder<Integer, String> lines = ImmutableMap.builder();
        int start = Math.max(frame.getLineNumber() - 10, 0);
        int end = start + 20;
        int current = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file.get()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (current < start) {
                    current += 1;
                    continue;
                }

                if (current > end) {
                    break;
                }

                lines.put(current, line);
                current += 1;
            }
        } catch (Exception e) {
            // If we get an IOException, not much we can do... just ignore it and move on.
            return Optional.absent();
        }

        return Optional.of(lines.build());
    }
}
