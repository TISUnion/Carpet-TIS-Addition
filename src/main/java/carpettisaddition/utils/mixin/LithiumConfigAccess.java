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

package carpettisaddition.utils.mixin;

import carpettisaddition.utils.ModIds;
import me.jellysquid.mods.lithium.common.config.LithiumConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.Nullable;

import java.io.File;

//#if MC >= 11600
//$$ import me.jellysquid.mods.lithium.common.config.Option;
//#endif

public class LithiumConfigAccess
{
	@Nullable
	private static final Object config;

	private static boolean isLithiumLoaded()
	{
		return FabricLoader.getInstance().isModLoaded(ModIds.lithium);
	}

	/**
	 * For 1.15.2 lithium, check {@link me.jellysquid.mods.lithium.mixin.LithiumMixinPlugin#setupMixins}
	 * and fill interested rules in the switch statement below
	 *
	 * For 1.16+ lithium, {@link me.jellysquid.mods.lithium.common.config.LithiumConfig#getEffectiveOptionForMixin} exists
	 * so no more manual maintaining
	 */
	public static boolean isLithiumMixinRuleEnabled(String mixinRule)
	{
		if (config == null)
		{
			return false;
		}

		//#if MC >= 11600
		//$$ // in lithium's usage, the passed argument is something like "block.stone.StoneOptimizationMixin"
		//$$ // so we need to attach a fake class name as suffix, to make sure lithium's logic works correctly
		//$$ Option option = ((LithiumConfig)config).getEffectiveOptionForMixin(mixinRule + ".TISCM_DummyMixin");
		//$$ return option != null && option.isEnabled();
		//#else
		switch (mixinRule)
		{
			default:
				return false;
		}
		//#endif
	}

	static
	{
		if (isLithiumLoaded())
		{
			config = LithiumConfig.load(new File("./config/lithium.properties"));
		}
		else
		{
			config = null;
		}
	}
}
