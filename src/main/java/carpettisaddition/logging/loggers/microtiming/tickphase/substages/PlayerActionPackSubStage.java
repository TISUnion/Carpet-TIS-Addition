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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.BaseComponent;

public class PlayerActionPackSubStage extends AbstractPlayerRelatedSubStage
{
	public PlayerActionPackSubStage(ServerPlayer player)
	{
		super(player);
	}

	@Override
	public BaseComponent toText()
	{
		return Messenger.c(
				tr("player_action_pack"), Messenger.newLine(),
				super.toText()
		);
	}
}
