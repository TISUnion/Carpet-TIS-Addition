/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.Vec3d;

public class LifetimeTexts
{
	public static final Translator t = LifeTimeTracker.getInstance().getTranslator();

	public static BaseText spawningPosButton(Vec3d spawningPos, DimensionWrapper dimension, String keyOverride)
	{
		BaseText spawningPosHover = Messenger.c(t.tr(keyOverride), "g : ", "w " + TextUtils.coord(spawningPos));
		if (CommandUtils.isConsoleCommandSource(LifeTimeTrackerContext.commandSource.get()))
		{
			return Messenger.c("e [", spawningPosHover, "e ]");
		}
		else
		{
			return Messenger.fancy(
					null,
					Messenger.s("[S]", "e"),
					spawningPosHover,
					Messenger.ClickEvents.suggestCommand(TextUtils.tp(spawningPos, dimension))
			);
		}
	}
	public static BaseText spawningPosButton(Vec3d spawningPos, DimensionWrapper dimension)
	{
		return spawningPosButton(spawningPos, dimension, "spawning_position");
	}

	public static BaseText removalPosButton(Vec3d removalPos, DimensionWrapper dimension)
	{
		BaseText removalPosHover = Messenger.c(t.tr("removal_position"), "g : ", "w " + TextUtils.coord(removalPos));
		if (CommandUtils.isConsoleCommandSource(LifeTimeTrackerContext.commandSource.get()))
		{
			return Messenger.c("r [", removalPosHover, "r ]");
		}
		else
		{
			return Messenger.fancy(
					null,
					Messenger.s("[R]", "r"),
					removalPosHover,
					Messenger.ClickEvents.suggestCommand(TextUtils.tp(removalPos, dimension))
			);
		}
	}
}
