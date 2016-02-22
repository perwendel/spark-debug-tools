package spark.debug;

import static spark.Spark.get;
import static spark.debug.DebugScreen.enableDebugScreen;

public class DebugScreenExample {
    public static void main(String[] args) {

        get("*", (req, res) -> {
            throw new Exception("Exceptions everywhere!");
        });

        enableDebugScreen(); //just add this to your project to enable the debug screen

    }
}
