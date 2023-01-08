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
import carpettisaddition.logging.loggers.microtiming.events.ExecuteBlockEventEvent;
import carpettisaddition.mixins.command.manipulate.container.ServerWorldAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockAction;
import net.minecraft.util.math.BlockPos;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.arguments.BlockStateArgumentType.blockState;
import static net.minecraft.command.arguments.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BlockEventQueueController extends AbstractContainerController
{
	public BlockEventQueueController()
	{
		super("block_event");
	}

	public int removeAt(ServerCommandSource source, BlockPos blockPos)
	{
		int counter = 0;
		ObjectLinkedOpenHashSet<BlockAction> queue = ((ServerWorldAccessor)source.getWorld()).getPendingBlockActions();
		for (ObjectListIterator<BlockAction> iterator = queue.iterator(); iterator.hasNext(); )
		{
			BlockAction be = iterator.next();
			if (be.getPos().equals(blockPos))
			{
				iterator.remove();
				counter++;
			}
		}
		Messenger.tell(source, Messenger.tr("removed", counter), true);
		return counter;
	}

	public int addEvent(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		BlockPos blockPos = getLoadedBlockPos(context, "pos");
		Block block = getBlockState(context, "block").getBlockState().getBlock();
		int type = getInteger(context, "type");
		int data = getInteger(context, "data");
		BlockAction blockAction = new BlockAction(blockPos, block, type, data);

		Messenger.tell(source, tr(
				"scheduled",
				Messenger.fancy(tr("item_name"), ExecuteBlockEventEvent.getMessageExtraMessengerHoverText(blockAction), null),
				Messenger.coord(blockPos, DimensionWrapper.of(source.getWorld()))
		), true);
		source.getWorld().addBlockAction(blockPos, block, type, data);
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
										then(argument("type", integer()).
												then(argument("data", integer()).
														executes(this::addEvent)
												)
										)
								)
						)
				);
	}
}
