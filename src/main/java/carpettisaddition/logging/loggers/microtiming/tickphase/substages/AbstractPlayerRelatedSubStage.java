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

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

public abstract class AbstractPlayerRelatedSubStage extends AbstractSubStage
{
	protected final ServerPlayerEntity player;

	public AbstractPlayerRelatedSubStage(ServerPlayerEntity player)
	{
		this.player = player;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.c(
				tr("player"),
				String.format("w : %s", this.player.getGameProfile().getName())
		);
	}

	@Override
	public ClickEvent getClickEvent()
	{
		return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, TextUtil.tp(this.player));
	}
}
