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

package carpettisaddition.commands.manipulate.container.controller;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import org.jetbrains.annotations.Nullable;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.arguments.BlockStateArgumentType.blockState;
import static net.minecraft.command.arguments.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//#if MC >= 11800
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//#else
import net.minecraft.server.world.ServerTickScheduler;
import java.util.List;
//#endif

public class TileTickQueueController extends AbstractContainerController
{
	public TileTickQueueController()
	{
		super("tile_tick");
	}

	private int remove(
			//#if MC >= 11800
			//$$ WorldTickScheduler<?> tickScheduler,
			//#else
			ServerTickScheduler<?> serverTickScheduler,
			//#endif
			BlockPos blockPos)
	{
		BlockBox blockBox =
				//#if MC >= 11700
				//$$ BlockBox.create
				//#else
				new BlockBox
				//#endif
						(blockPos, blockPos.add(1, 1, 1));

		//#if MC >= 11800
		//$$ int sizeBefore = tickScheduler.getTickCount();
		//$$ tickScheduler.clearNextTicks(blockBox);
		//$$ int sizeAfter = tickScheduler.getTickCount();
		//$$ return sizeBefore - sizeAfter;
		//#else
		List<?> removed = serverTickScheduler.getScheduledTicks(blockBox, true, false);
		return removed.size();
		//#endif
	}

	private int removeAt(ServerCommandSource source, BlockPos blockPos)
	{
		int counter = 0;
		counter += this.remove(source.getWorld().getBlockTickScheduler(), blockPos);
		counter += this.remove(source.getWorld().getFluidTickScheduler(), blockPos);
		Messenger.tell(source, tr("removed", counter), true);
		return counter;
	}

	private int addTileTickEvent(CommandContext<ServerCommandSource> context, @Nullable Object priorityArg) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		BlockPos blockPos = getLoadedBlockPos(context, "pos");
		Block block = getBlockState(context, "block").getBlockState().getBlock();
		int delay = getInteger(context, "delay");
		TickPriority priority = TickPriority.NORMAL;
		if (priorityArg instanceof TickPriority)
		{
			priority = (TickPriority)priorityArg;
		}
		if (priorityArg instanceof Integer)
		{
			priority = TickPriority.byIndex((Integer)priorityArg);
		}

		source.getWorld().
				//#if MC >= 11800
				//$$ createAndScheduleBlockTick
				//#else
				getBlockTickScheduler().schedule
				//#endif
				(blockPos, block, delay, priority);
		Messenger.tell(source, tr(
				"scheduled",
				Messenger.fancy(tr("item_name"), tr("item_description", Messenger.block(block), delay, priority.getIndex(), priority), null),
				Messenger.coord(blockPos, DimensionWrapper.of(source.getWorld()))
		), true);
		return 1;
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode(CommandTreeContext context)
	{
		return super.getCommandNode(context).
				then(literal("remove").
						then(argument("pos", blockPos()).
								executes(c -> this.removeAt(c.getSource(), getLoadedBlockPos(c, "pos")))
						)
				).
				then(literal("add").
						then(argument("pos", blockPos()).
								then(argument("block", blockState(
												//#if MC >= 11900
												//$$ context.commandBuildContext
												//#endif
										)).
										then(argument("delay", integer()).
												executes(c -> this.addTileTickEvent(c, null)).
												then(argument("priority", integer(TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex())).
														executes(c -> this.addTileTickEvent(c, getInteger(c, "priority")))
												)
										)
								)
						)
				);
	}
}
