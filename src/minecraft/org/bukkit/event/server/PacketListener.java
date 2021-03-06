package org.bukkit.event.server;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class PacketListener {
    public static Map<Integer, Collection<PacketListener>> incomingPacketListeners = new HashMap();
    public static Map<Integer, Collection<PacketListener>> outgoingPacketListeners = new HashMap();

    static Map<PacketListener, Plugin> listenerToPlugin = new HashMap();

    public static void addPacketListener(boolean outgoing, int packetID, PacketListener listener, Plugin plugin) {
        Map packetListeners = outgoing ? outgoingPacketListeners : incomingPacketListeners;

        Collection listenersForPacket = (Collection) packetListeners.get(Integer.valueOf(packetID));
        if (listenersForPacket == null) {
            packetListeners.put(Integer.valueOf(packetID), listenersForPacket = new HashSet());
        }

        listenersForPacket.add(listener);

        listenerToPlugin.put(listener, plugin);
    }

    public static void removePacketListener(boolean outgoing, int packetID, PacketListener listener) {
        Map packetListeners = outgoing ? outgoingPacketListeners : incomingPacketListeners;

        Collection listenersForPacket = (Collection) packetListeners.get(Integer.valueOf(packetID));
        if (listenersForPacket == null) {
            return;
        }

        listenersForPacket.remove(listenersForPacket);
    }

    public static final void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        for (Iterator it = listenerToPlugin.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry foo = (Map.Entry) it.next();

            PacketListener packetListener = (PacketListener) foo.getKey();

            if (foo.getValue() == plugin) {
                for (Map<Integer, Collection<PacketListener>> packetListenerMaps : Arrays.asList(outgoingPacketListeners, incomingPacketListeners)) {
                    for (Map.Entry<Integer, Collection<PacketListener>> packetListeners : packetListenerMaps.entrySet()) {
                        packetListeners.getValue().remove(packetListener);
                    }
                }
            }

            it.remove();
        }
    }

    public boolean onOutgoingPacket(Player ply, int packetID, Packet packet) {
        System.err.println("onOutgoingPacket not overridden.");
        removePacketListener(true, packetID, this);

        return true;
    }

    public boolean onIncomingPacket(Player ply, int packetID, Packet packet) {
        System.err.println("onIncomingPacket not overridden.");
        removePacketListener(false, packetID, this);

        return true;
    }
}