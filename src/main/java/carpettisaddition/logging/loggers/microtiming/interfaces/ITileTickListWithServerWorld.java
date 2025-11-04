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

package carpettisaddition.logging.loggers.microtiming.interfaces;

import carpettisaddition.mixins.logger.microtiming.hooks.ServerWorldMixin;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

/**
 * Used in mc 1.18+, where the game's tiletick list class doesn't store related ServerLevel object
 */
public interface ITileTickListWithServerWorld
{
	/**
	 * Nullable if it's a custom ServerTickScheduler
	 * Since only value in vanilla ServerTickScheduler (block, fluid) has been assigned in {@link ServerWorldMixin}
	 */
	@Nullable
	ServerLevel getServerWorld$TISCM();

	void setServerWorld$TISCM(ServerLevel serverWorld);
}