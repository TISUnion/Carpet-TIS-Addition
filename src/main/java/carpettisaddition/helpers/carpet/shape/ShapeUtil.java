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
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Maps;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
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

	public static ShapeHolder<ShapeDispatcher.Line> createLine(Vec3d from, Vec3d to, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("line"));
		boxParams.put("from", posToList(from));
		boxParams.put("to", posToList(to));
		return new ShapeHolder<>(ShapeDispatcherLineAccessor.invokeConstructor(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.Box> createBox(Vec3d from, Vec3d to, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("box"));
		boxParams.put("from", posToList(from));
		boxParams.put("to", posToList(to));
		return new ShapeHolder<>(new ShapeDispatcher.Box(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.Sphere> createSphere(Vec3d center, float radius, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> boxParams = getBasicParamMap(dimension, color);
		boxParams.put("shape", new StringValue("sphere"));
		boxParams.put("center", posToList(center));
		boxParams.put("radius", new NumericValue(radius));
		return new ShapeHolder<>(ShapeDispatcherSphereAccessor.invokeConstructor(), boxParams);
	}

	public static ShapeHolder<ShapeDispatcher.DisplayedText> createLabel(Text text, Vec3d pos, DimensionWrapper dimension, @Nullable Long color)
	{
		Map<String, Value> textParams = getBasicParamMap(dimension, color);
		textParams.put("shape", new StringValue("label"));
		textParams.put("pos", posToList(pos));
		textParams.put("text", new FormattedTextValue(text));
		textParams.put("align", new StringValue(ScarpetDisplayedTextHack.MICRO_TIMING_TEXT_MAGIC_STRING));
		return new ShapeHolder<>(new ShapeDispatcher.DisplayedText(), textParams);
	}

	private static ListValue posToList(Vec3d vec3d)
	{
		return ListValue.of(new NumericValue(vec3d.getX()), new NumericValue(vec3d.getY()), new NumericValue(vec3d.getZ()));
	}
}
