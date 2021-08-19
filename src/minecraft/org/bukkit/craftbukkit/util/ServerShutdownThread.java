package org.bukkit.craftbukkit.util;


import net.minecraft.server.MinecraftServer;

public class ServerShutdownThread extends Thread {
    private final net.minecraft.server.MinecraftServer server;

    public ServerShutdownThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            server.stopServer();
        } catch (net.minecraft.world.MinecraftException ex) {
            ex.printStackTrace();
        } finally {
            try {
                server.reader.getTerminal().restore();
            } catch (Exception e) {
            }
        }
    }
}
