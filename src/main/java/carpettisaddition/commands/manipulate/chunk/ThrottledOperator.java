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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PositionUtils;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.world.level.ChunkPos;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

class ThrottledOperator extends TranslationContext implements Operator
{
	private final Semaphore semaphore = new Semaphore(1);
	private final AtomicReference<AbortableOperation> currentOperation = new AtomicReference<>(null);
	private final OperateImpl operateImpl;

	@FunctionalInterface
	interface OperateImpl
	{
		AbortableOperation createAndRun(CommandSourceStack source, List<ChunkPos> chunkPosList, Runnable doneCallback);
	}

	public ThrottledOperator(ChunkManipulator chunkManipulator, OperateImpl operateImpl)
	{
		super(chunkManipulator.getTranslator());
		this.operateImpl = operateImpl;
	}

	public int operate(CommandContext<CommandSourceStack> ctx, List<ChunkPos> chunkPosList)
	{
		if (!this.semaphore.tryAcquire())
		{
			Messenger.tell(ctx.getSource(), this.tr("common.throttled"));
			return 0;
		}
		try
		{
			AbortableOperation operation = this.operateImpl.createAndRun(ctx.getSource(), chunkPosList, () -> {
				this.currentOperation.set(null);
				this.semaphore.release();
			});
			this.currentOperation.set(operation);
			return chunkPosList.size();
		}
		catch (Throwable e)
		{
			CarpetTISAdditionMod.LOGGER.error("Manipulation error", e);
			this.semaphore.release();
			throw e;
		}
	}

	public int abort(CommandSourceStack source)
	{
		AbortableOperation operation = currentOperation.get();
		if (operation == null)
		{
			Messenger.tell(source, this.tr("common.nothing_to_abort"));
			return 0;
		}
		else
		{
			operation.abort(source);
			return 1;
		}
	}
}
