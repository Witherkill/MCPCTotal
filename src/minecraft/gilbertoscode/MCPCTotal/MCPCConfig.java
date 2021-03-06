package gilbertoscode.MCPCTotal;

import com.google.common.base.Throwables;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class MCPCConfig {

    private static final File CONFIG_FILE = new File("TotalMCPC.yml");
    private static final String HEADER = "This is the main configuration file for TotalMCPC.\n"
            + "\n"
            + "If you need help with the configuration or have any questions related to TotalMCPC,\n"
            + "Discord: ! Gilberto#0159.\n";
    public static boolean dumpMaterials = false;
    public static boolean loadChunkOnRequest = false;
    public static boolean disableWarnings = false;
    public static boolean worldLeakDebug = false;
    // Some mods such as Twilight Forest listen for specific events as their WorldProvider loads to hotload its dimension. This prevents this from happening so MV can create worlds using the same provider without issue.
    public static boolean craftWorldLoading = false;
    public static boolean connectionLogging = false;
    public static boolean tileEntityPlaceLogging = true;
    public static boolean infiniteWaterSource = true;
    public static boolean flowingLavaDecay = false;
    public static boolean fakePlayerLogin = false;
    public static boolean remapPluginFile = false;
    /*========================================================================*/
    static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/

    public static void init() {
        if (config == null) {
            config = YamlConfiguration.loadConfiguration(CONFIG_FILE);
            config.options().header(HEADER);
            config.options().copyDefaults(true);
            commands = new HashMap<String, Command>();
            version = getInt(1);
            set("config-version", 1);
            dumpMaterials = getBoolean("settings.dump-materials", false); // dumps all materials with their corresponding id's
            loadChunkOnRequest = getBoolean("settings.load-chunk-on-request", false); // sets ChunkProvideServer.loadChunkProvideOnRequest
            disableWarnings = getBoolean("logging.disabled-warnings", false); // disable warning messages to server admins
            worldLeakDebug = getBoolean("logging.world-leak-debug", false);
            connectionLogging = getBoolean("logging.connection", false);
            infiniteWaterSource = getBoolean("world-settings.default.infinite-water-source", true);
            flowingLavaDecay = getBoolean("world-settings.default.flowing-lava-decay", false);
            tileEntityPlaceLogging = getBoolean("logging.warn-place-no-tileentity", true);
            fakePlayerLogin = getBoolean("fake-players.do-login", false);
            remapPluginFile = getBoolean("plugin-settings.default.remap-plugin-file", false);
            if (loadChunkOnRequest && !disableWarnings) {
                Bukkit.getLogger().severe("This version of MCPC+ handles chunk loading better if load-chunk-on-request is set to false. Please consider changing this value in your bukkit.yml");
            }
            readConfig(MCPCConfig.class, null);
        }
    }

    public static void save() {
        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    static void readConfig(Class<?> clazz, Object instance) {
        init();
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    public static boolean getBoolean(String path, boolean def) {
        return getBoolean(path, def, true);
    }

    public static boolean getBoolean(String path, boolean def, boolean addDefault) {
        if (addDefault)
            config.addDefault(path, def);
        return config.getBoolean(path, def);
    }

    private static int getInt(int def) {
        config.addDefault("config-version", def);
        return config.getInt("config-version", config.getInt("config-version"));
    }

    public static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return config.getList(path, config.getList(path));
    }

    public static String getString(String path, String def) {
        return getString(path, def, true);
    }

    public static String getString(String path, String def, boolean addDefault) {
        if (addDefault)
            config.addDefault(path, def);
        return config.getString(path, def);
    }

    public static String getFakePlayer(String className, String defaultName) {
        return getString("fake-players." + className + ".username", defaultName);
    }
}
