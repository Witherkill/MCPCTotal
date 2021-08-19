package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenShrub extends WorldGenerator implements net.minecraft.block.BlockSapling.TreeGenerator   // CraftBukkit add interface
{
    private int field_76527_a;
    private int field_76526_b;

    public WorldGenShrub(int par1, int par2)
    {
        this.field_76526_b = par1;
        this.field_76527_a = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        // CraftBukkit start - Moved to generate
        return this.generate((BlockChangeDelegate) par1World, par2Random, par3, par4, par5);
    }

    public boolean generate(BlockChangeDelegate par1World, Random par2Random, int par3, int par4, int par5)
    {
        // CraftBukkit end
        int l;

        Block block = null;
        do 
        {
            block = Block.blocksList[par1World.getTypeId(par3,  par4, par5)];
            if (block != null && !block.isAirBlock(par1World, par3, par4, par5) && !block.isLeaves(par1World, par3, par4, par5))
            {
                break;
            }
            par4--;
        } while (par4 > 0);

        int i1 = par1World.getTypeId(par3, par4, par5);

        if (i1 == Block.dirt.blockID || i1 == Block.grass.blockID)
        {
            ++par4;
            this.setTypeAndData(par1World, par3, par4, par5, Block.wood.blockID, this.field_76526_b);

            for (int j1 = par4; j1 <= par4 + 2; ++j1)
            {
                int k1 = j1 - par4;
                int l1 = 2 - k1;

                for (int i2 = par3 - l1; i2 <= par3 + l1; ++i2)
                {
                    int j2 = i2 - par3;

                    for (int k2 = par5 - l1; k2 <= par5 + l1; ++k2)
                    {
                        int l2 = k2 - par5;

                        block = Block.blocksList[par1World.getTypeId(i2, j1, k2)];

                        if ((Math.abs(j2) != l1 || Math.abs(l2) != l1 || par2Random.nextInt(2) != 0) && 
                            (block == null || block.canBeReplacedByLeaves(par1World, i2, j1, k2)))
                        {
                            this.setTypeAndData(par1World, i2, j1, k2, Block.leaves.blockID, this.field_76527_a);
                        }
                    }
                }
            }

            // CraftBukkit start - Return false if gen was unsuccessful
        }
        else
        {
            return false;
        }

        // CraftBukkit end
        return true;
    }
}
