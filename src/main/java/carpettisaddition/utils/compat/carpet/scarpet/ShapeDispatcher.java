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

package carpettisaddition.utils.compat.carpet.scarpet;

import carpet.script.value.Value;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Map;

//#if MC < 11700
import org.apache.commons.lang3.tuple.Pair;
//#endif

public class ShapeDispatcher
{
	//#if MC >= 11700
	//$$ public static class ShapeWithConfig
	//$$ {
	//$$ }
	//#endif

	public static void sendShape(
			List<ServerPlayerEntity> subscribedPlayers,
			//#if MC >= 11700
			//$$ List<ShapeWithConfig> shapeDataList
			//#else
			List<Pair<ExpiringShape, Map<String, Value>>> shapeDataList
			//#endif
	)
	{
	}

	public static class ExpiringShape
	{
		protected void init(Map<String, Value> options)
		{

		}
	}

	public static class Line extends ExpiringShape
	{
		private Line()
		{
		}
	}

	public static class Box extends ExpiringShape
	{
	}

	public static class Sphere extends ExpiringShape
	{
		private Sphere()
		{
		}
	}

	public static class Text extends ExpiringShape
	{
	}

	public static class FormattedTextParam
	{
	}
}