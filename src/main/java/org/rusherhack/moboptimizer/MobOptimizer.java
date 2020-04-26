package org.rusherhack.moboptimizer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author John200410 4/23/2020 for OptimizeMobs
 */
public class MobOptimizer extends JavaPlugin {

	@Override
	public void onEnable() {
		Bukkit.getServer().getScheduler().runTaskTimer(this, () -> {
			Bukkit.getServer().getScheduler().runTaskAsynchronously(this, () -> {
				for (World world : Bukkit.getWorlds()) {
					for (Entity en : world.getEntities()) {
						if(en instanceof LivingEntity) {
							LivingEntity entity = (LivingEntity) en;
							if(shouldLimitEntity(entity)) {
								entity.setAI(false);
							} else if(!entity.hasAI()) {
								entity.setAI(true);
							}
						}
					}
				}
			});
		}, 0L, 100L);
	}

	private boolean shouldLimitEntity(LivingEntity entity) {
		switch (entity.getType()) {
			case BAT:
			case RABBIT:
			case POLAR_BEAR:
			case ENDERMITE:
			case ARMOR_STAND:
				return true;
			default:
		}

		if(/* entity has nametag */entity.getCustomName() != null || entity instanceof Wolf || entity instanceof Parrot || entity.fromMobSpawner() && getPlayerCount() < 110) {
			return false;
		} else if(entity instanceof Skeleton) {
			return isEntityNearPlayer(entity, 10);
		} else if(!isEntityNearPlayer(entity, 40)) {
			return true;
		} else if(entity instanceof Monster) {
			return getTps() < 10 || getPlayerCount() > 90;
		} else {
			return getTps() < 6 || getPlayerCount() > 120;
		}
	}

	private int getPlayerCount() {
		return Bukkit.getServer().getOnlinePlayers().size();
	}

	private int getTps() {
		return (int) Bukkit.getServer().getTPS()[0];
	}

	private boolean isEntityNearPlayer(LivingEntity entity, double distance) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if(player.getNearbyEntities(distance, distance, distance).contains(entity)) {
				return true;
			}
		}
		return false;
	}

}
