package org.bukkit.craftbukkit.entity;


import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Minecart;
import org.bukkit.util.Vector;

public class CraftMinecart extends CraftVehicle implements Minecart { // MCPC+ - concrete for modded minecarts
    public CraftMinecart(CraftServer server, net.minecraft.entity.item.EntityMinecart entity) {
        super(server, entity);
    }

    // MCPC+ start
    public org.bukkit.entity.EntityType getType() {
        return org.bukkit.entity.EntityType.MINECART;
    }
    // MCPC+ end

    public int getDamage() {
        return getHandle().getDamage();
    }

    public void setDamage(int damage) {
        getHandle().setDamage(damage);
    }

    public double getMaxSpeed() {
        return getHandle().maxSpeed;
    }

    public void setMaxSpeed(double speed) {
        if (speed >= 0D) {
            getHandle().maxSpeed = speed;
        }
    }

    public boolean isSlowWhenEmpty() {
        return getHandle().slowWhenEmpty;
    }

    public void setSlowWhenEmpty(boolean slow) {
        getHandle().slowWhenEmpty = slow;
    }

    public Vector getFlyingVelocityMod() {
        return getHandle().getFlyingVelocityMod();
    }

    public void setFlyingVelocityMod(Vector flying) {
        getHandle().setFlyingVelocityMod(flying);
    }

    public Vector getDerailedVelocityMod() {
        return getHandle().getDerailedVelocityMod();
    }

    public void setDerailedVelocityMod(Vector derailed) {
        getHandle().setDerailedVelocityMod(derailed);
    }

    @Override
    public net.minecraft.entity.item.EntityMinecart getHandle() {
        return (net.minecraft.entity.item.EntityMinecart) entity;
    }
}
