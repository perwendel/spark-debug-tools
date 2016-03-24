package spark.debug;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.debug.DebugScreen.enableDebugScreen;

public class DebugScreenExample {
    public static void main(String[] args) {
        port(4567);
        before((request, response) -> {
            response.cookie("example-cookie", "example-cookie-content");
            request.attribute("example-request-attr", "example-attr-content");
            request.session().attribute("example-session-attr", "example-session-attr-content");
        });
        get("*", (req, res) -> {
            return Integer.parseInt("lol");
        });
        enableDebugScreen(); //just add this to your project to enable the debug screen
    }
}
