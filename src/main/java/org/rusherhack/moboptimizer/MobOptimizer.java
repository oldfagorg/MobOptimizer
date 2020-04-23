package org.rusherhack.moboptimizer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * this is a plugin i wrote in about 25 minutes to help with the lag on oldfag.org
 * works better than i expected
 *
 * @author John200410 4/23/2020 for OptimizeMobs
 */
public class MobOptimizer extends JavaPlugin {

	@Override
	public void onDisable() {
	}

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
			case SNOWMAN:
			case RABBIT:
			case POLAR_BEAR:
			case ENDERMITE:
			case ARMOR_STAND:
				return true;
			default:
		}
		if(entity.fromMobSpawner() && getPlayerCount() < 100) {
			return false;
		} else if(entity instanceof Monster) {
			return getTps() < 10 || getPlayerCount() > 90;
		} else {
			return getTps() < 6 || getPlayerCount() > 110;
		}
	}

	private int getPlayerCount() {
		return Bukkit.getServer().getOnlinePlayers().size();
	}

	private int getTps() {
		return (int) Bukkit.getServer().getTPS()[0];
	}

}
