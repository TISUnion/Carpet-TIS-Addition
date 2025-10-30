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

package carpettisaddition.logging.loggers.microtiming.enums;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.Translator;
import net.minecraft.network.chat.BaseComponent;

public enum TickStage
{
	UNKNOWN(false),
	SPAWNING(true),
	SPAWNING_SPECIAL(true),
	WORLD_BORDER(true),
	TILE_TICK(true),
	RAID(true),
	WANDERING_TRADER(true),  // It's not used in 1.16+ where it's included in SPAWNING_SPECIAL in 1.16
	BLOCK_EVENT(true),
	ENTITY(true),
	CHUNK_TICK(true),
	TILE_ENTITY(true),
	DRAGON_FIGHT(true),
	AUTO_SAVE(false),
	ASYNC_TASK(false),
	PLAYER_ACTION(false),
	COMMAND_FUNCTION(false),
	NETWORK(false),
	CONSOLE(false),
	SCARPET(false);

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("stage");
	private final String translationKey;
	private final boolean insideWorld;

	TickStage(boolean insideWorld)
	{
		this.translationKey = this.name().toLowerCase();
		this.insideWorld = insideWorld;
	}

	public BaseComponent toText()
	{
		return translator.tr(this.translationKey);
	}

	public boolean isInsideWorld()
	{
		return this.insideWorld;
	}
}
