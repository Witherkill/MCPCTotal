/*
 * Forge Mod Loader
 * Copyright (c) 2012-2013 cpw.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     cpw - implementation
 */

package cpw.mods.fml.common;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FMLLog {
    private static final cpw.mods.fml.relauncher.FMLRelaunchLog coreLog = cpw.mods.fml.relauncher.FMLRelaunchLog.log;

    public static void log(String logChannel, Level level, String format, Object... data) {
        String format1 = format;
        if (level == Level.SEVERE || level == Level.WARNING) {
            format1 =  "\u001B[31m" + format1;
        }
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            format1 = "\u001B[36m" + format1;
        }
        FMLRelaunchLog.log(logChannel, level, format1, data);
    }

    public static void log(Level level, String format, Object... data) {
        String format1 = format;
        if (level == Level.SEVERE || level == Level.WARNING) {
            format1 =  "\u001B[31m" + format1;
        }
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            format1 = "\u001B[36m" + format1;
        }
        FMLRelaunchLog.log(level, format1, data);
    }

    public static void log(String logChannel, Level level, Throwable ex, String format, Object... data) {
        String format1 = format;
        if (level == Level.SEVERE || level == Level.WARNING) {
            format1 =  "\u001B[31m" + format1;
        }
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            format1 = "\u001B[36m" + format1;
        }
        FMLRelaunchLog.log(logChannel, level, ex, format1, data);
    }

    public static void log(Level level, Throwable ex, String format, Object... data) {
        String format1 = format;
        if (level == Level.SEVERE || level == Level.WARNING) {
            format1 =  "\u001B[31m" + format1;
        }
        if (level == Level.INFO || level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            format1 = "\u001B[36m" + format1;
        }
        FMLRelaunchLog.log(level, ex, format1, data);
    }

    public static void severe(String format, Object... data) {
        String format1 = format;
        log(Level.SEVERE, "\u001B[31m" + format1, data);
    }

    public static void warning(String format, Object... data) {
        String format1 = format;
        log(Level.WARNING, "\u001B[31m" + format1, data);
    }

    public static void info(String format, Object... data) {
        String format1 = format;
        log(Level.INFO, "\u001B[36m" + format1, data);
    }

    public static void fine(String format, Object... data) {
        String format1 = format;
        log(Level.FINE, "\u001B[36m" + format1, data);
    }

    public static void finer(String format, Object... data) {
        String format1 = format;
        log(Level.FINER, "\u001B[36m" + format1, data);
    }

    public static void finest(String format, Object... data) {
        String format1 = format;
        log(Level.FINEST, "\u001B[36m" + format1, data);
    }

    public static Logger getLogger() {
        return coreLog.getLogger();
    }

    public static void makeLog(String logChannel) {
        FMLRelaunchLog.makeLog(logChannel);
    }
}
