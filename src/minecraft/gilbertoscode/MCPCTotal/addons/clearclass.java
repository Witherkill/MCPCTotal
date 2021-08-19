package gilbertoscode.MCPCTotal.addons;

import java.io.File;
import java.net.URISyntaxException;

public class clearclass {
    public static void limparfolder() throws URISyntaxException {
        final File maindir = new File(clearclass.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String[] entries = maindir.list();
        for (String s : entries) {
            if (s.split("\\.")[1] != null && s.split("\\.")[1].equals("class")) {
                File a = new File(maindir.getPath(), s);
                a.delete();
            }
        }
    }
}
