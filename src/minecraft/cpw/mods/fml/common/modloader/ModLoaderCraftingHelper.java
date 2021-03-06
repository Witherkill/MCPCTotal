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

package cpw.mods.fml.common.modloader;

import cpw.mods.fml.common.ICraftingHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ModLoaderCraftingHelper implements ICraftingHandler {

    private final BaseModProxy mod;

    public ModLoaderCraftingHelper(BaseModProxy mod) {
        this.mod = mod;
    }

    @Override
    public void onCrafting(EntityPlayer player, ItemStack item, IInventory craftMatrix) {
        mod.takenFromCrafting(player, item, craftMatrix);
    }

    @Override
    public void onSmelting(EntityPlayer player, ItemStack item) {
        mod.takenFromFurnace(player, item);
    }

}
