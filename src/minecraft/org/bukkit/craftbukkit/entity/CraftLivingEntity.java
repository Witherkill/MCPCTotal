package org.bukkit.craftbukkit.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.*;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final net.minecraft.entity.EntityLiving entity) {
        super(server, entity);

        if (!(this instanceof HumanEntity)) {
            equipment = new CraftEntityEquipment(this);
        }
    }

    public int getHealth() {
        return Math.min(Math.max(0, getHandle().getHealth()), getMaxHealth());
    }

    public void setHealth(int health) {
        if ((health < 0) || (health > getMaxHealth())) {
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth());
        }

        if (entity instanceof net.minecraft.entity.player.EntityPlayerMP && health == 0) {
            ((net.minecraft.entity.player.EntityPlayerMP) entity).onDeath(net.minecraft.util.DamageSource.generic);
        }

        getHandle().setEntityHealth(health);
    }

    public int getMaxHealth() {
        return getHandle().maxHealth;
    }

    public void setMaxHealth(int amount) {
        Validate.isTrue(amount > 0, "Max health must be greater than 0");

        getHandle().maxHealth = amount;

        if (getHealth() > amount) {
            setHealth(amount);
        }
    }

    public void resetMaxHealth() {
        setMaxHealth(getHandle().getMaxHealth());
    }

    @Deprecated
    public Egg throwEgg() {
        return launchProjectile(Egg.class);
    }

    @Deprecated
    public Snowball throwSnowball() {
        return launchProjectile(Snowball.class);
    }

    public double getEyeHeight() {
        return getHandle().getEyeHeight();
    }

    public double getEyeHeight(boolean ignoreSneaking) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            int id = block.getTypeId();
            if (transparent == null) {
                if (id != 0) {
                    break;
                }
            } else {
                if (!transparent.contains((byte) id)) {
                    break;
                }
            }
        }
        return blocks;
    }

    public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    @Deprecated
    public Arrow shootArrow() {
        return launchProjectile(Arrow.class);
    }

    public int getRemainingAir() {
        return getHandle().getAir();
    }

    public void setRemainingAir(int ticks) {
        getHandle().setAir(ticks);
    }

    public int getMaximumAir() {
        return getHandle().maxAirTicks;
    }

    public void setMaximumAir(int ticks) {
        getHandle().maxAirTicks = ticks;
    }

    public void damage(int amount) {
        damage(amount, null);
    }

    public void damage(int amount, org.bukkit.entity.Entity source) {
        net.minecraft.util.DamageSource reason = net.minecraft.util.DamageSource.generic;

        if (source instanceof HumanEntity) {
            reason = net.minecraft.util.DamageSource.causePlayerDamage(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = net.minecraft.util.DamageSource.causeMobDamage(((CraftLivingEntity) source).getHandle());
        }

        if (entity instanceof net.minecraft.entity.boss.EntityDragon) {
            ((net.minecraft.entity.boss.EntityDragon) entity).func_82195_e(reason, amount);
        } else {
            entity.attackEntityFrom(reason, amount);
        }
    }

    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return getHandle().maxHurtResistantTime;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().maxHurtResistantTime = ticks;
    }

    public int getLastDamage() {
        return getHandle().lastDamage;
    }

    public void setLastDamage(int damage) {
        getHandle().lastDamage = damage;
    }

    public int getNoDamageTicks() {
        return getHandle().hurtResistantTime;
    }

    public void setNoDamageTicks(int ticks) {
        getHandle().hurtResistantTime = ticks;
    }

    @Override
    public net.minecraft.entity.EntityLiving getHandle() {
        return (net.minecraft.entity.EntityLiving) entity;
    }

    public void setHandle(final net.minecraft.entity.EntityLiving entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityId() + '}';
    }

    public Player getKiller() {
        return getHandle().attackingPlayer == null ? null : (Player) getHandle().attackingPlayer.getBukkitEntity();
    }

    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        getHandle().addPotionEffect(new net.minecraft.potion.PotionEffect(effect.getType().getId(), effect.getDuration(), effect.getAmplifier()));
        return true;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().isPotionActive(net.minecraft.potion.Potion.potionTypes[type.getId()]);
    }

    public void removePotionEffect(PotionEffectType type) {
        getHandle().activePotionsMap.remove(type.getId());
        getHandle().potionsNeedUpdate = true;
        if (getHandle() instanceof net.minecraft.entity.player.EntityPlayerMP) {
            if (((net.minecraft.entity.player.EntityPlayerMP) getHandle()).playerNetServerHandler == null) return;
            ((net.minecraft.entity.player.EntityPlayerMP) getHandle()).playerNetServerHandler.sendPacketToPlayer(new net.minecraft.network.packet.Packet42RemoveEntityEffect(getHandle().entityId, new net.minecraft.potion.PotionEffect(type.getId(), 0, 0)));
        }
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (Object raw : getHandle().activePotionsMap.values()) {
            if (!(raw instanceof net.minecraft.potion.PotionEffect))
                continue;
            net.minecraft.potion.PotionEffect handle = (net.minecraft.potion.PotionEffect) raw;
            effects.add(new PotionEffect(PotionEffectType.getById(handle.getPotionID()), handle.getDuration(), handle.getAmplifier()));
        }
        return effects;
    }

    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        net.minecraft.world.World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntitySnowball(world, getHandle());
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityEgg(world, getHandle());
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.item.EntityEnderPearl(world, getHandle());
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityArrow(world, getHandle(), 1);
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            launch = new net.minecraft.entity.projectile.EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(Material.POTION, 1)));
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.entity.projectile.EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new net.minecraft.entity.projectile.EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new net.minecraft.entity.projectile.EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }

            launch.setLocationAndAngles(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");

        world.spawnEntityInWorld(launch);
        return (T) launch.getBukkitEntity();
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return getHandle().getEntitySenses().canSee(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return !getHandle().persistenceRequired;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        getHandle().persistenceRequired = !remove;
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public boolean getCanPickupItems() {
        return getHandle().canPickUpLoot;
    }

    public void setCanPickupItems(boolean pickup) {
        getHandle().canPickUpLoot = pickup;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    public String getCustomName() {
        String name = getHandle().func_94057_bL();

        if (name == null || name.length() == 0) {
            return null;
        }

        return name;
    }

    public void setCustomName(String name) {
        if (name == null) {
            name = "";
        }

        // Names cannot be more than 64 characters due to DataWatcher limitations
        if (name.length() > 64) {
            name = name.substring(0, 64);
        }

        getHandle().func_94058_c(name);
    }

    public boolean isCustomNameVisible() {
        return getHandle().func_94062_bN();
    }

    public void setCustomNameVisible(boolean flag) {
        getHandle().func_94061_f(flag);
    }
}
