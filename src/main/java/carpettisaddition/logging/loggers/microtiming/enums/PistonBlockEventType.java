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
import net.minecraft.text.BaseText;

public enum PistonBlockEventType
{
	PUSH,
	RETRACT,
	DROP,
	UNKNOWN;

	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("piston_block_event_type");
	private static final PistonBlockEventType[] VALUES = PistonBlockEventType.values();
	private final String translationKey;

	PistonBlockEventType()
	{
		this.translationKey = this.name().toLowerCase();
	}

	public BaseText toText()
	{
		return translator.tr(this.translationKey);
	}

	public static PistonBlockEventType fromId(int id)
	{
		return 0 <= id && id <= 2 ? VALUES[id] : UNKNOWN;
	}
}
