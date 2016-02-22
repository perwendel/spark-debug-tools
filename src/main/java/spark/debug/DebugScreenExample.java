package spark.debug;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.debug.DebugScreen.enableDebugScreen;

public class DebugScreenExample {

    public static void main(String[] args) {

        port(8080);

        get("/", (req, res) -> {
            req.session().attribute("hello", "person");
            return "Hello!";
        });

        get("/except/:p", (req, res) -> {
            req.attribute("hello", "world");
            throw new Exception("Testing Handler!");
        });

        enableDebugScreen(); //just add this line to your project to enable the debug screen
    }

}
