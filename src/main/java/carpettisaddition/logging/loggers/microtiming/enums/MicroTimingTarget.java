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

import carpettisaddition.utils.Messenger;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public enum MicroTimingTarget
{
	LABELLED,
	IN_RANGE,
	ALL,
	MARKER_ONLY;

	public static final double IN_RANGE_RADIUS = 32.0D;

	public static void deprecatedWarning(@Nullable ServerCommandSource source)
	{
		if (source != null)
		{
			BaseText text = Messenger.tr("carpettisaddition.rule.microTimingTarget.deprecate_not_marker_warning");
			Messenger.tell(source, Messenger.formatting(text, Formatting.DARK_RED, Formatting.ITALIC));
		}
	}
}
