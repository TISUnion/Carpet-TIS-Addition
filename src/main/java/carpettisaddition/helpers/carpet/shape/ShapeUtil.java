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

package carpettisaddition.helpers.carpet.shape;

//#if MC < 11500
//$$ import carpettisaddition.utils.compat.carpet.scarpet.FormattedTextValue;
//#endif

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.*;
import carpettisaddition.mixins.carpet.shape.ShapeDispatcherLineAccessor;
import carpettisaddition.mixins.carpet.shape.ShapeDispatcherSphereAccessor;
import carpettisaddition.network.TISCMFeature;
import carpettisaddition.network.TISCMServerPacketHandler;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ShapeUtil
{
	private static Map<String, Value> getBasicParamMap(DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> params = Maps.newHashMap();
		params.put("dim", new StringValue(dimension.getIdentifierString()));
		params.put("duration", new NumericValue(Integer.MAX_VALUE));
		if (color != null)
		{
			params.put("color", new NumericValue(color));
		}
		return params;
	}

	public static ShapeHolder<ShapeDispatcher.Line> createLine(Vec3 from, Vec3 to, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("line"));
		boxParams.put("from", posToList(from));
		boxParams.put("to", posToList(to));
		return new ShapeHolder<>(ShapeDispatcherLineAccessor.invokeConstructor(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.Box> createBox(Vec3 from, Vec3 to, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("box"));
		boxParams.put("from", posToList(from));
		boxParams.put("to", posToList(to));
		return new ShapeHolder<>(new ShapeDispatcher.Box(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.Sphere> createSphere(Vec3 center, float radius, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("sphere"));
		boxParams.put("center", posToList(center));
		boxParams.put("radius", new NumericValue(radius));
		return new ShapeHolder<>(ShapeDispatcherSphereAccessor.invokeConstructor(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.Text> createLabel(Component text, Vec3 pos, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> textParams = getBasicParamMap(dimension, color);
		textParams.put("shape", new StringValue("label"));
		textParams.put("pos", posToList(pos));
		textParams.put("text", new FormattedTextValue(text));
		textParams.put("align", new StringValue(ScarpetDisplayedTextHack.MICRO_TIMING_TEXT_MAGIC_STRING));
		return new ShapeHolder<>(new ShapeDispatcher.Text(), textParams);
	}

	private static ListValue posToList(Vec3 vec3d)
	{
		return ListValue.of(new NumericValue(vec3d.x()), new NumericValue(vec3d.y()), new NumericValue(vec3d.z()));
	}

	public static boolean supportsPreciseShapes(ServerPlayer player)
	{
		return TISCMServerPacketHandler.getInstance().getClientFeatureOrDefault(player.connection, TISCMFeature.PRECISE_SCARPET_SHAPES, 0) >= 1;
	}

	public static ListTag prepareShapesForPlayer(ServerPlayer player, ListTag shapes)
	{
		if (ShapeUtil.supportsPreciseShapes(player))
		{
			return shapes;
		}
		ListTag copy = null;
		for (int i = 0; i < shapes.size(); i++)
		{
			CompoundTag shape = (CompoundTag)shapes.get(i);
			if (shape.contains(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY))
			{
				if (copy == null)
				{
					copy = shapes.copy();
				}
				((CompoundTag)copy.get(i)).remove(ShapeDispatcherPreciseExpiringShape.PRECISE_NBT_KEY);
			}
		}
		return copy != null ? copy : shapes;
	}
}
