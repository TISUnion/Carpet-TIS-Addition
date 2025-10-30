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

package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.world.phys.Vec3;

public class EntitySubStage extends AbstractSubStage
{
	private final Entity entity;
	private final int order;
	private final Vec3 pos;

	public EntitySubStage(Entity entity, int order)
	{
		this.entity = entity;
		this.order = order;
		this.pos = entity.position();
	}

	@Override
	public BaseComponent toText()
	{
		return Messenger.c(
				MicroTimingLoggerManager.tr("common.entity"), "w : ", this.entity.getDisplayName(), "w \n",
				MicroTimingLoggerManager.tr("common.type"), "w : ", Messenger.entityType(this.entity), "w \n",
				MicroTimingLoggerManager.tr("common.order"), String.format("w : %d\n", this.order),
				MicroTimingLoggerManager.tr("common.position"), String.format("w : %s", TextUtils.coord(this.pos))
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return Messenger.ClickEvents.suggestCommand(TextUtils.tp(this.entity));
	}
}
