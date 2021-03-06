package gilbertoscode.MCPCTotal.potion;

import net.minecraft.item.crafting.IRecipe;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

public class CustomModRecipe implements Recipe {
    private final IRecipe iRecipe;
    public CustomModRecipe(IRecipe iRecipe) {
        this.iRecipe = iRecipe;
    }
    @Override
    public ItemStack getResult() {
        return CraftItemStack.asCraftMirror(iRecipe.getRecipeOutput());
    }
    public IRecipe getHandle() {
        return iRecipe;
    }
}
