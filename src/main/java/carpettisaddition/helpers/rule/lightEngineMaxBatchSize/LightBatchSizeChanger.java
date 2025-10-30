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

package carpettisaddition.helpers.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.level.ServerLevel;

/**
 * Used in MC < 1.16 only
 * MC >= 1.16 fabric carpet introduces the lightEngineMaxBatchSize rule
 * MC 23w17a (1.20 snapshot) changes batch size to 1000, and fabric carpet disables the rule (at least for now)
 */
public class LightBatchSizeChanger
{
	public static void setSize(int newSize)
	{
		//#if MC < 11600
		if (CarpetTISAdditionServer.minecraft_server != null)
		{
			for (ServerLevel serverWorld : CarpetTISAdditionServer.minecraft_server.getAllLevels())
			{
				serverWorld.getChunkSource().getLightEngine().setTaskPerBatch(newSize);
			}
		}
		//#endif
	}

	public static void apply()
	{
		//#if MC < 11600
		setSize(CarpetTISAdditionSettings.lightEngineMaxBatchSize);
		//#endif
	}
}
