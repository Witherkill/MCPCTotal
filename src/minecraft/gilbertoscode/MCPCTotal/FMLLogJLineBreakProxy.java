// MCPC+ - proxy for FML log / CB jline compatibility
// TODO: move out of NMS namespace
package gilbertoscode.MCPCTotal;

import jline.console.ConsoleReader;

import java.io.IOException;

public class FMLLogJLineBreakProxy {
    public static ConsoleReader reader = null;
    public static void consoleReaderResetPreLog() {
        if (reader != null) {
            try {
                reader.print("\r");
                reader.flush();
            } catch (IOException ignored) {
            }
        }
    }
    public static void consoleReaderResetPostLog() {
        if (reader != null) {
            try {
                reader.drawLine();
            } catch (Throwable ex) {
                reader.getCursorBuffer().clear();
            }
            try {
                reader.flush();
            } catch (IOException ignored) {
            }
        }
    }
}
