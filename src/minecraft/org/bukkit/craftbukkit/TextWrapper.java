package org.bukkit.craftbukkit;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TextWrapper {
    public static List<String> wrapText(final String text) {
        ArrayList<String> output = new ArrayList<String>();
        String[] lines = text.split("\n");
        String lastColor = null;

        for (String line : lines) {
            if (lastColor != null) {
                line = lastColor + line;
            }

            output.add(line);
            lastColor = ChatColor.getLastColors(line);
        }

        return output;
    }
}
