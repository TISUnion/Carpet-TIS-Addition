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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.translations.TranslationContext;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

class SimpleOperator extends TranslationContext implements Operator
{
	private final OperateImpl operateImpl;

	@FunctionalInterface
	interface OperateImpl
	{
		int run(CommandContext<CommandSourceStack> ctx, List<ChunkPos> chunkPosList) throws CommandSyntaxException;
	}

	public SimpleOperator(ChunkManipulator chunkManipulator, OperateImpl operateImpl)
	{
		super(chunkManipulator.getTranslator());
		this.operateImpl = operateImpl;
	}

	@Override
	public int operate(CommandContext<CommandSourceStack> ctx, List<ChunkPos> chunkPosList) throws CommandSyntaxException
	{
		return this.operateImpl.run(ctx, chunkPosList);
	}
}
