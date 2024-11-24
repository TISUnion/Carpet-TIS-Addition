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

package carpettisaddition.commands.manipulate.block;

import carpettisaddition.mixins.command.manipulate.block.FillCommandAccessor;
import carpettisaddition.utils.PositionUtils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;

public class BlockExecutor
{
	private final ExecuteImpl impl;
	private final int limit;

	public BlockExecutor(ExecuteImpl impl, int limit)
	{
		this.impl = impl;
		this.limit = limit;
	}

	public BlockExecutor(ExecuteImpl impl)
	{
		this(impl, 1000000);  // TODO, configure-able with rule
	}

	@FunctionalInterface
	public interface ExecuteImpl
	{
		void executeAt(CommandContext<ServerCommandSource> ctx, BlockPos blockPos);
	}

	public int process(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
	{
		BlockPos from = getLoadedBlockPos(ctx, "from");
		BlockPos to = getLoadedBlockPos(ctx, "to");
		BlockBox box = PositionUtils.createBlockBox(from, to);
		long blockCount = (long)box.getBlockCountX() * box.getBlockCountX() * box.getBlockCountZ();
		if (blockCount >= this.limit)
		{
			throw FillCommandAccessor.getTooBigException().create(this.limit, blockCount);
		}

		BlockPos.stream(from, to).forEach(blockPos -> this.impl.executeAt(ctx, blockPos));
		return (int)blockCount;
	}
}
