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

package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.gson.JsonObject;
import net.minecraft.network.chat.BaseComponent;

import java.util.Objects;

public class TransDimensionSpawningReason extends SpawningReason
{
	private final DimensionWrapper oldDimension;

	public TransDimensionSpawningReason(DimensionWrapper oldDimension)
	{
		this.oldDimension = Objects.requireNonNull(oldDimension);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TransDimensionSpawningReason that = (TransDimensionSpawningReason) o;
		return Objects.equals(this.oldDimension, that.oldDimension);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.oldDimension);
	}

	@Override
	public BaseComponent toText()
	{
		return Messenger.c(
				tr("trans_dimension"),
				"g  (",
				Messenger.formatting(tr("trans_dimension.from", Messenger.dimension(this.oldDimension)), "g"),
				"g )"
		);
	}

	@Override
	public String getRecordId()
	{
		return "trans_dimension";
	}

	@Override
	public JsonObject getRecordData()
	{
		JsonObject data = new JsonObject();
		data.addProperty("fromDimension", this.oldDimension.getIdentifierString());
		return data;
	}
}
