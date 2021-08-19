package org.bukkit.craftbukkit.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class CraftEnderDragon extends CraftComplexLivingEntity implements EnderDragon {
    public CraftEnderDragon(CraftServer server, net.minecraft.entity.boss.EntityDragon entity) {
        super(server, entity);
    }

    public Set<ComplexEntityPart> getParts() {
        Builder<ComplexEntityPart> builder = ImmutableSet.builder();

        for (net.minecraft.entity.boss.EntityDragonPart part : getHandle().dragonPartArray) {
            builder.add((ComplexEntityPart) part.getBukkitEntity());
        }

        return builder.build();
    }

    @Override
    public net.minecraft.entity.boss.EntityDragon getHandle() {
        return (net.minecraft.entity.boss.EntityDragon) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderDragon";
    }

    public EntityType getType() {
        return EntityType.ENDER_DRAGON;
    }
}
