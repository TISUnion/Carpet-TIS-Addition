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

package carpettisaddition;

import carpettisaddition.utils.AutoMixinAuditExecutor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarpetTISAdditionMod implements ModInitializer
{
	public static final String MOD_ID = "carpet-tis-addition";
	public static final String MOD_NAME = "Carpet TIS Addition";
	public static final String COMPACT_NAME = MOD_ID.replace("-","");  // carpettisaddition
	private static String version;

	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

	@Override
	public void onInitialize()
	{
		version = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

		CarpetTISAdditionServer.init();
		AutoMixinAuditExecutor.run();
	}

	public static String getVersion()
	{
		return version;
	}
}
