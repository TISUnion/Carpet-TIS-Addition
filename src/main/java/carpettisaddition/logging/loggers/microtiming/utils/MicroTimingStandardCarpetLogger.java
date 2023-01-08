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

package carpettisaddition.logging.loggers.microtiming.utils;

//#if MC >= 11500
import carpet.logging.Logger;
//#else
//$$ import carpettisaddition.logging.compat.ExtensionLogger;
//#endif

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.MicroTimingTarget;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;

import java.lang.reflect.Field;
import java.util.Arrays;

public class MicroTimingStandardCarpetLogger extends
		//#if MC >= 11500
		Logger
		//#else
		//$$ ExtensionLogger
		//#endif
{
	private static final MicroTimingStandardCarpetLogger INSTANCE = createInstance();

	public static final String NAME = MicroTimingLogger.NAME;
	private static final Translator translator = MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("carpet_logger");

	private MicroTimingStandardCarpetLogger(Field acceleratorField, String logName, String def, String[] options)
	{
		super(
				acceleratorField, logName, def, options
				//#if MC >= 11700
				//$$ , true
				//#endif
		);
	}

	private static MicroTimingStandardCarpetLogger createInstance()
	{
		String def = MicroTimingLogger.LoggingOption.DEFAULT.toString();
		String[] options = Arrays.stream(MicroTimingLogger.LoggingOption.values()).map(MicroTimingLogger.LoggingOption::toString).map(String::toLowerCase).toArray(String[]::new);
		return new MicroTimingStandardCarpetLogger(TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, def, options);
	}

	public static MicroTimingStandardCarpetLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void addPlayer(String playerName, String option)
	{
		super.addPlayer(playerName, option);
		ServerPlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			if (MicroTimingLoggerManager.isLoggerActivated())
			{
				if (CarpetTISAdditionSettings.microTimingTarget != MicroTimingTarget.MARKER_ONLY)
				{
					MicroTimingTarget.deprecatedWarning(player.getCommandSource());
				}
			}
			else
			{
				String command = "/carpet microTiming true";
				Messenger.tell(
						player,
						Messenger.fancy(
								translator.tr("rule_hint", command),
								translator.tr("click_to_execute"),
								new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
						)
				);
			}
			MicroTimingMarkerManager.getInstance().sendAllMarkersForPlayer(player);
		}
	}

	@Override
	public void removePlayer(String playerName)
	{
		super.removePlayer(playerName);
		ServerPlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			MicroTimingMarkerManager.getInstance().cleanAllMarkersForPlayer(player);
		}
	}

	public void onCarpetClientHello(ServerPlayerEntity player)
	{
		if (MicroTimingUtil.isPlayerSubscribed(player))
		{
			MicroTimingMarkerManager.getInstance().sendAllMarkersForPlayer(player);
		}
	}
}
