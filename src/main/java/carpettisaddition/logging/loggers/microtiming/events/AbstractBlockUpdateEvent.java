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

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.world.level.block.Block;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractBlockUpdateEvent extends BaseEvent
{
	protected final Block sourceBlock;
	protected final BlockUpdateType blockUpdateType;
	protected final Direction exceptSide;
	@Nullable
	private BaseComponent updateTypeExtraMessageCache;

	public AbstractBlockUpdateEvent(EventType eventType, String translateKey, Block sourceBlock, BlockUpdateType blockUpdateType, Direction exceptSide)
	{
		super(eventType, translateKey, sourceBlock);
		this.sourceBlock = sourceBlock;
		this.blockUpdateType = blockUpdateType;
		this.exceptSide = exceptSide;
	}

	protected BaseComponent getUpdateTypeExtraMessage()
	{
		if (this.updateTypeExtraMessageCache == null)
		{
			this.updateTypeExtraMessageCache = this.blockUpdateType.getUpdateOrderList(this.exceptSide);
		}
		return this.updateTypeExtraMessageCache;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof AbstractBlockUpdateEvent)) return false;
		if (!super.equals(o)) return false;
		AbstractBlockUpdateEvent that = (AbstractBlockUpdateEvent) o;
		return blockUpdateType == that.blockUpdateType &&
				Objects.equals(this.getUpdateTypeExtraMessage(), that.getUpdateTypeExtraMessage()) &&
				Objects.equals(sourceBlock, that.sourceBlock);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), sourceBlock, blockUpdateType, exceptSide);
	}
}
