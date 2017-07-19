Spark Debug Screen
=====================
Error pages for the [Spark Java micro-framework](http://sparkjava.com/).

![image](http://i.imgur.com/Z3MdIsI.png)

## Usage:
To utilize:
```java
package spark.debug;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.debug.DebugScreen.enableDebugScreen;

public class DebugScreenExample {
    public static void main(String[] args) {

        get("*", (req, res) -> {
            throw new Exception("Exceptions everywhere!");
        });
        
        enableDebugScreen(); //just add this to your project to enable the debug screen
        
    }
}
```

## Maven:
```xml
<dependency>
    <groupId>com.sparkjava</groupId>
    <artifactId>spark-debug-tools</artifactId>
    <version>0.5</version>
</dependency>
```
## Advanced Usage:

**To add additional tables:**

```java

// Subclass the handler:
class MyDebugScreen extends DebugScreen {
  @Override
  protected void installTables(LinkedHashMap<String, Map<String, ? extends Object>> tables, Request request, Throwable exception) {
    super.installTables(tables, request, exception);
    Map<String, Object> myTable = new LinkedHashMap<>();
    tables.put("My Table", myTable);
    myTable.put("Key", "Value");
  }
}

// When installing the exception handler, install yours instead:
Spark.exception(Exception.class, new MyDebugScreen());

```

**To change the search path for locating Java source files:**

By default DebugScreen looks within the folders `src/main/java` and `src/test/java` in the current working directory (if they exist). If you have changed the working directory, obviously this approach will not work. You can specify different search directories:

```java
Spark.exception(Exception.class, new DebugScreen(
    ImmutableList.of(new LocalSourceLocator(new File("/path/to/source/code")))
));
```

You can specify multiple locators in the list (later ones are used as fallbacks if earlier ones cannot find a file). If this is still not specific enough for you, you can implement your own `SourceLocator` to find the files and provide that to the handler. This could even fetch files over the network if needed.

## Notes:

* This handler reveals server internals and possibly code. Only install it when you are developing and make sure to disable it before pushing to production.
* Finding code snippets is an imperfect art since the original file locations are not preserved in the compiled bytecode. Thus, by default code snippets will only be displayed if the Java source file for the corresponding exception stack frame is available within the working directory under `src/main/java` or `src/main/test` and you are properly following the Java naming and directory structure conventions. See advanced usage to change this behavior.
* Registering the exception handler for `Exception.class` serves as a catch-all. You can still install your own, more specific exception handlers. Spark prioritizes exception handlers by crawling up the class hierarchy from the exception thrown to `Exception` looking for a handler. Thus, handlers for subclasses of `Exception` will take precendence.

## Credits:

By [mschurr](https://github.com/mschurr/). Based on [filp/whoops](http://filp.github.io/whoops/).
