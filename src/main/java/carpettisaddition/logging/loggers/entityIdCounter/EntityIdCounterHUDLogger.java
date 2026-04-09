/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.logging.loggers.entityIdCounter;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.GameUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.deobfuscator.StackTracePrinter;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

//#if MC >= 1.21.2
//$$ import net.minecraft.TracingExecutor;
//#endif

public class EntityIdCounterHUDLogger extends AbstractHUDLogger
{
	public static final String NAME = "entityIdCounter";

	private static final EntityIdCounterHUDLogger INSTANCE = new EntityIdCounterHUDLogger();
	private final EntityIdCounterSampler entityIdCounterSampler = new EntityIdCounterSampler();

	private EntityIdCounterHUDLogger()
	{
		super(NAME, true);
	}

	public static EntityIdCounterHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public String getDefaultLoggingOption()
	{
		return LoggingType.HUD.getName();
	}

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return LoggingType.LOGGING_SUGGESTIONS;
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		if (!LoggingType.HUD.isContainedIn(option))
		{
			return null;
		}

		int value = EntityIdCounterUtils.getCurrentEntityIdCounterValue();
		double overflowTo0Percent = EntityIdCounterUtils.getPercentOfOverflowToZero(value);
		ChatFormatting percentColor = overflowTo0Percent > 99.99 ? ChatFormatting.RED : (overflowTo0Percent > 99.9 ? ChatFormatting.YELLOW : ChatFormatting.GRAY);

		BaseComponent incrementSpeed = Messenger.s("-", ChatFormatting.DARK_GRAY);
		OptionalDouble ratePerGtOpt = this.entityIdCounterSampler.getRatePerGt();
		if (ratePerGtOpt.isPresent())
		{
			double speedPerSec = ratePerGtOpt.getAsDouble() * 20;
			double hours = (1L << 32) / Math.max(0.123, speedPerSec * 3600);  // hours to overflow a complete 2^32
			incrementSpeed = Messenger.s(
					speedPerSec < 10 ? String.format("%.1f/s", speedPerSec) : String.format("%.0f/s", speedPerSec),
					hours < 24 ? ChatFormatting.GOLD :
							hours < 24 * 30 ? ChatFormatting.WHITE :
							speedPerSec >= 1e-6 ? ChatFormatting.GRAY :
							ChatFormatting.DARK_GRAY
			);
		}

		return new BaseComponent[]{
				Messenger.join(
						Messenger.s(" "),
						Messenger.s("EID", ChatFormatting.BLUE),
						Messenger.s(value, ChatFormatting.GRAY),
						Messenger.s(String.format("%.2f%%", overflowTo0Percent), percentColor),
						incrementSpeed
				)
		};
	}

	public void tick()
	{
		int value = EntityIdCounterUtils.getCurrentEntityIdCounterValue();
		this.entityIdCounterSampler.recordForOneTick(value);
	}

	private BaseComponent pack(BaseComponent message)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", Messenger.s("EID", ChatFormatting.BLUE), "g ] "),
						Messenger.c(tr("header_hover"), "w \n", Messenger.s(command)),
						Messenger.ClickEvents.suggestCommand(command)
				),
				message
		);
	}

	public void onEntityIdCounterAdded(Entity entity)
	{
		if (!TISAdditionLoggerRegistry.__entityIdCounter)
		{
			return;
		}

		Thread currentThread = Thread.currentThread();
		BaseComponent threadName = Messenger.hover(
				Util.make(() -> {
					if (GameUtils.isOnServerThread())
					{
						return Messenger.formatting(tr("thread_name.server"), ChatFormatting.GREEN);
					}
					else if (GameUtils.isOnClientThread())
					{
						return Messenger.formatting(tr("thread_name.client"), ChatFormatting.GOLD);
					}
					else
					{
						Executor backgroundExecutor = Util.backgroundExecutor();
						if (currentThread instanceof ForkJoinWorkerThread)
						{
							ForkJoinPool fjp = ((ForkJoinWorkerThread)currentThread).getPool();
							if (
									backgroundExecutor == fjp
									//#if MC >= 1.21.2
									//$$ || (backgroundExecutor instanceof TracingExecutor te && te.service() == fjp)
									//#endif
							)
							{
								return Messenger.formatting(tr("thread_name.worker"), ChatFormatting.AQUA);
							}
						}
					}
					return Messenger.formatting(tr("thread_name.unknown"), ChatFormatting.GRAY);
				}),
				Messenger.s(currentThread.getName())
		);

		// can't use `Messenger.entity()` since `entity.getName()` might not be available yet
		// e.g., for Players there might be a 'Cannot invoke "com.mojang.authlib.GameProfile.getName()" because "this.gameProfile" is null' error
		BaseComponent entityName = Messenger.fancy(
				"b",
				Messenger.entityType(entity),
				Messenger.s(entity.getStringUUID()),
				Messenger.ClickEvents.suggestCommand(TextUtils.tpUuid(entity))
		);
		BaseComponent entityId = Messenger.s(entity.getId(), ChatFormatting.YELLOW);
		BaseComponent stackTraceSymbol = StackTracePrinter.makeSymbol(this.getClass());

		Runnable doLog = () -> this.logToChat(option -> {
			if (!LoggingType.INCREMENT.isContainedIn(option))
			{
				return null;
			}
			return new BaseComponent[]{pack(Messenger.c(
					tr("increment", entityName, entityId, threadName),
					Messenger.s(" "),
					stackTraceSymbol
			))};
		});

		MinecraftServer server = CarpetTISAdditionServer.minecraft_server;
		if (server != null)
		{
			GameUtils.scheduleOnServerThread(server, doLog);
		}
		else if (GameUtils.isOnServerThread())  // just in case
		{
			doLog.run();
		}
	}

	private enum LoggingType
	{
		HUD,
		INCREMENT,
		ALL;

		public static final	String[] LOGGING_SUGGESTIONS;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean isContainedIn(String option)
		{
			return ALL.getName().equals(option) || Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}

		static
		{
			LOGGING_SUGGESTIONS = Arrays.stream(values()).map(LoggingType::getName).toArray(String[]::new);
		}
	}
}
