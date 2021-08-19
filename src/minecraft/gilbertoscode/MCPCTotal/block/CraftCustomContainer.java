package gilbertoscode.MCPCTotal.block;

import net.minecraft.inventory.IInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftCustomContainer extends CraftBlockState implements InventoryHolder {
    private final net.minecraft.inventory.IInventory container;
    public CraftCustomContainer(Block block) {
        super(block);
        CraftWorld world = (CraftWorld) block.getWorld();
        container = (IInventory) world.getTileEntityAt(getX(), getY(), getZ());
    }
    @Override
    public Inventory getInventory() {
        return new CraftInventory(container);
    }
}
