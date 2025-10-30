/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.info.entity;

import carpettisaddition.utils.ItemUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

//#if MC >= 12100
//$$ import net.minecraft.registry.entry.RegistryEntry;
//#endif

/**
 * Logic ports from fabric-carpet 1.4.54
 * Newer carpet remove entity info framework, so we need to keep and use a copy of that
 */
public class EntityInfoUtil
{
	public static String makeTime(long ticks)
	{
		long secs = ticks/20;
		if (secs < 60)
		{
			return String.format("%d\"", secs);
		}
		if (secs < 60*60)
		{
			return String.format("%d'%d\"", secs/60, secs%60);
		}

		return String.format("%dh%d'%d\"", secs/60/60, (secs % (60*60))/60,(secs % (60*60))%60 );
	}

	public static String display_item(LivingEntity owner /*used in mc1.21+*/, ItemStack item)
	{
		if (item == null)
		{
			return null;
		}
		if (item.isEmpty()) // func_190926_b()
		{
			return null;
		} // func_190916_E()
		String stackname = item.getCount()>1?String.format("%dx%s",item.getCount(), item.getHoverName().getString()):item.getHoverName().getString();
		if (item.isDamaged())
		{
			//#if MC >= 12100
			//$$ int maxUseTime = item.getMaxUseTime(owner);
			//#else
			int maxUseTime = item.getUseDuration();
			//#endif
			stackname += String.format(" %d/%d", maxUseTime - item.getDamageValue(), maxUseTime);
		}
		if (item.isEnchanted())
		{
			stackname += " ( ";
			Map<Enchantment, Integer> enchants = ItemUtils.getEnchantments(item);
			for (Enchantment e: enchants.keySet())
			{
				int level = enchants.get(e);
				//#if MC >= 12100
				//$$ String enstring = Enchantment.getName(RegistryEntry.of(e), level).getString();
				//#else
				String enstring = e.getFullname(level).getString();
				//#endif
				stackname += enstring+" ";
			}
			stackname += ")";
		}
		return stackname;
	}

	public static String entity_short_string(Entity e)
	{
		if (e == null)
		{
			return "None";
		}
		return String.format(
				"%s at [%.1f, %.1f, %.1f]",
				e.getDisplayName().getString(),
				//#if MC >= 11500
				e.getX(), e.getY(), e.getZ()
				//#else
				//$$ e.x, e.y, e.z
				//#endif
		);
	}

	public static double get_speed(double internal)
	{
		return 43.1*internal;
	}

	public static double get_horse_speed_percent(double internal)
	{
		double min = 0.45*0.25;
		double max = (0.45+0.9)*0.25;
		return 100*(internal-min)/(max-min);
	}

	public static double get_horse_jump(double x)
	{
		return -0.1817584952 * x*x*x + 3.689713992 * x*x + 2.128599134 * x - 0.343930367;
	}

	public static double get_horse_jump_percent(double internal)
	{
		double min = 0.4;
		double max = 1.0;
		return 100*(internal-min)/(max-min);
	}
}