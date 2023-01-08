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

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import carpettisaddition.mixins.carpet.shape.ExpiringShapeInvoker;
import com.google.common.collect.Maps;

import java.util.Map;

//#if MC < 11700
import org.apache.commons.lang3.tuple.Pair;
//#endif

public class ShapeHolder<T extends ShapeDispatcher.ExpiringShape>
{
	public final T shape;
	public final Map<String, Value> params;
	public final Map<String, Value> emptyParams;

	public ShapeHolder(T shape, Map<String, Value> params)
	{
		this.shape = shape;
		this.params = params;
		this.emptyParams = Maps.newHashMap(this.params);
		this.emptyParams.put("duration", new NumericValue(0));
		this.updateShape();
	}

	private void updateShape()
	{
		// the shape instance is useful for non-carpet players
		((ExpiringShapeInvoker) this.shape).callInit(this.params);
	}

	public
	//#if MC >= 11700
	//$$ ShapeDispatcher.ShapeWithConfig
	//#else
	Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>
	//#endif
	toPair(boolean display)
	{
		return
				//#if MC >= 11700
				//$$ new ShapeDispatcher.ShapeWithConfig
				//#else
				Pair.of
				//#endif
						(this.shape, display ? this.params : this.emptyParams);
	}

	public void setValue(String key, Value value)
	{
		this.params.put(key, value);
		this.emptyParams.put(key, value);
		this.updateShape();
	}
}
