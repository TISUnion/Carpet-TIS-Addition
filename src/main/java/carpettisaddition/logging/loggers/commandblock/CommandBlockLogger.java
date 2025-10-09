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

package carpettisaddition.logging.loggers.commandblock;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.WorldUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.text.BaseText;
import net.minecraft.util.ChatUtil;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;

public class CommandBlockLogger extends AbstractLogger
{
	public static final String NAME = "commandBlock";
	public static final int MINIMUM_LOG_INTERVAL = 3 * 20;  // 3s
	public static final int MAXIMUM_PREVIEW_LENGTH = 16;
	private static final CommandBlockLogger INSTANCE = new CommandBlockLogger();

	public CommandBlockLogger()
	{
		super(NAME, true);
	}

	public static CommandBlockLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return new String[]{"throttled", "all"};
	}

	private void logCommandBlockExecution(World world, BaseText nameText, BaseText posText, CommandBlockExecutor executor, String removeCommand)
	{
		if (!TISAdditionLoggerRegistry.__commandBlock)
		{
			return;
		}

		ICommandBlockExecutor iExecutor = (ICommandBlockExecutor)executor;
		long time = WorldUtils.getWorldTime(world);
		String commandPreview = executor.getCommand();
		if (commandPreview.length() > MAXIMUM_PREVIEW_LENGTH)
		{
			commandPreview = commandPreview.substring(0, MAXIMUM_PREVIEW_LENGTH - 3) + "...";
		}
		String finalCommandPreview = commandPreview;

		this.log((option) -> {
			boolean isThrottledLogging = !option.equals("all");
			if (time - iExecutor.getLastLoggedTime() < MINIMUM_LOG_INTERVAL && isThrottledLogging)
			{
				return null;
			}
			if (isThrottledLogging)
			{
				iExecutor.setLastLoggedTime(time);
			}
			return new BaseText[]{Messenger.c(
					tr(
							"executed",
							Messenger.formatting(Messenger.copy(nameText), Formatting.GOLD),
							Messenger.fancy(
									"c",
									Messenger.s(finalCommandPreview),
									Messenger.s(executor.getCommand()),
									Messenger.ClickEvents.suggestCommand(executor.getCommand())
							)
					),
					"g  @ ",
					posText,
					"w  ",
					Messenger.fancy(
							"r",
							Messenger.s("[×]"),
							tr("remove_executor", nameText),
							Messenger.ClickEvents.suggestCommand(removeCommand)
					)
			)};
		});
	}

	public void onCommandBlockActivated(World world, BlockPos pos, BlockState state, CommandBlockExecutor executor)
	{
		this.logCommandBlockExecution(
				world,
				Messenger.block(state.getBlock()),
				Messenger.coord("w", pos, DimensionWrapper.of(world)),
				executor,
				String.format("/execute in %s run setblock %d %d %d %s", DimensionWrapper.of(world).getIdentifier(), pos.getX(), pos.getY(), pos.getZ(), Registry.BLOCK.getId(Blocks.AIR))
		);
	}

	public void onCommandBlockMinecartActivated(CommandBlockMinecartEntity entity)
	{
		if (ChatUtil.isEmpty(entity.getCommandExecutor().getCommand()))
		{
			return;
		}
		this.logCommandBlockExecution(
				EntityUtils.getEntityWorld(entity),
				Messenger.entity(null, entity),
				Messenger.coord("w", entity.getPos(), DimensionWrapper.of(entity)),
				entity.getCommandExecutor(),
				String.format("/kill %s", entity.getUuidAsString())
		);
	}
}
