package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.Spigot;

import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchdogThread extends Thread {

    private static WatchdogThread instance;
    private final long timeoutTime;
    private final boolean restart;
    private volatile long lastTick;
    private volatile boolean stopping;

    private WatchdogThread(long timeoutTime, boolean restart) {
        super("Spigot Watchdog Thread");
        this.timeoutTime = timeoutTime;
        this.restart = restart;
    }

    public static void doStart(int timeoutTime, boolean restart) {
        if (instance == null) {
            instance = new WatchdogThread(timeoutTime * 1000L, restart);
            instance.start();
        }
    }

    public static void tick() {
        instance.lastTick = System.currentTimeMillis();
    }

    public static void doStop() {
        if (instance != null) {
            instance.stopping = true;
        }
    }

    private static void dumpThread(ThreadInfo thread, Logger log) {
        if (thread.getThreadState() != State.WAITING) {
            log.log(Level.SEVERE, "------------------------------");
            //
            log.log(Level.SEVERE, "Current Thread: " + thread.getThreadName());
            log.log(Level.SEVERE, "\tPID: " + thread.getThreadId()
                    + " | Suspended: " + thread.isSuspended()
                    + " | Native: " + thread.isInNative()
                    + " | State: " + thread.getThreadState());
            if (thread.getLockedMonitors().length != 0) {
                log.log(Level.SEVERE, "\tThread is waiting on monitor(s):");
                for (MonitorInfo monitor : thread.getLockedMonitors()) {
                    log.log(Level.SEVERE, "\t\tLocked on:" + monitor.getLockedStackFrame());
                }
            }
            log.log(Level.SEVERE, "\tStack:");
            //
            StackTraceElement[] stack = thread.getStackTrace();
            for (int line = 0; line < stack.length; line++) {
                log.log(Level.SEVERE, "\t\t" + stack[line].toString());
            }
        }
    }

    @Override
    public void run() {
        while (!stopping) {
            //
            if (lastTick != 0 && System.currentTimeMillis() > lastTick + timeoutTime) {
                Logger log = Bukkit.getServer().getLogger();
                log.log(Level.SEVERE, "The server has stopped responding!");
                log.log(Level.SEVERE, "Please report this to discord ! Gilberto#0159");
                log.log(Level.SEVERE, "Be sure to include ALL relevant console errors and Minecraft crash reports");
                log.log(Level.SEVERE, "TotalMCPC version: " + Bukkit.getServer().getVersion());
                //
                log.log(Level.SEVERE, "------------------------------");
                log.log(Level.SEVERE, "Server thread dump (Look for plugins here before reporting to MCPC+!):");
                dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(MinecraftServer.getServer().primaryThread.getId(), Integer.MAX_VALUE), log);
                log.log(Level.SEVERE, "------------------------------");
                //
                log.log(Level.SEVERE, "Current Thread State:");
                ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
                for (ThreadInfo thread : threads) {
                    dumpThread(thread, log);
                }
                log.log(Level.SEVERE, "------------------------------");

                if (restart) {
                    Spigot.restart();
                }
                break;
            }

            try {
                sleep(10000);
            } catch (InterruptedException ex) {
                interrupt();
            }
        }
    }
}
