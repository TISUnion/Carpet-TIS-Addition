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

package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EmitBlockUpdateRedstoneDustEvent extends EmitBlockUpdateEvent
{
	private final List<BlockPos> updateOrder;
	private final BlockPos pos;

	public EmitBlockUpdateRedstoneDustEvent(EventType eventType, Block block, String methodName, BlockPos pos, Collection<BlockPos> updateOrder)
	{
		super(eventType, block, methodName);
		this.updateOrder = updateOrder != null ? Lists.newArrayList(updateOrder) : null;
		this.pos = pos.immutable();
	}

	@Override
	protected BaseComponent getUpdatesTextHoverText()
	{
		BaseComponent hover = super.getUpdatesTextHoverText();
		if (this.updateOrder != null)
		{
			List<Object> list = Lists.newArrayList();
			for (int i = 0; i < this.updateOrder.size(); i++)
			{
				list.add("w \n");
				BlockPos target = this.updateOrder.get(i);
				Vec3i vec = target.subtract(this.pos);
				Direction direction = Direction.fromNormal(
						vec.getX(), vec.getY(), vec.getZ()
						//#if MC >= 12102
						//$$ , null
						//#endif
				);
				list.add(String.format("w %d. ", i + 1));
				list.add(Messenger.coord("w", target));
				BaseComponent extra = null;
				if (direction != null)
				{
					extra = MicroTimingUtil.getFormattedDirectionText(direction);
				}
				if (target.equals(this.pos))
				{
					extra = tr("self");
				}
				if (extra != null)
				{
					list.add("w  ");
					list.add(extra);
				}
			}
			hover.append(Messenger.c(list.toArray(new Object[0])));
		}
		return hover;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof EmitBlockUpdateRedstoneDustEvent)) return false;
		if (!super.equals(o)) return false;
		EmitBlockUpdateRedstoneDustEvent that = (EmitBlockUpdateRedstoneDustEvent) o;
		return Objects.equals(updateOrder, that.updateOrder) &&
				Objects.equals(pos, that.pos);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), updateOrder, pos);
	}
}
