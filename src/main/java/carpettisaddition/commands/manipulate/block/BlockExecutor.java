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

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.command.manipulate.block.FillCommandAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PositionUtils;
import carpettisaddition.utils.TextUtils;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;

import java.util.Collections;
import java.util.List;

import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.getLoadedBlockPos;

public class BlockExecutor extends TranslationContext
{
	private final ExecuteImpl impl;
	private final int limit;
	private final String messageKey;
	private final MessageExtraArgsGetter messageExtraArgsGetter;

	public BlockExecutor(ExecuteImpl impl, int limit, String messageKey, MessageExtraArgsGetter messageExtraArgsGetter)
	{
		super(BlockManipulator.getInstance().getTranslator());
		this.impl = impl;
		this.limit = limit;
		this.messageKey = messageKey;
		this.messageExtraArgsGetter = messageExtraArgsGetter;
	}

	public BlockExecutor(ExecuteImpl impl, String messageKey, MessageExtraArgsGetter messageExtraArgsGetter)
	{
		this(impl, CarpetTISAdditionSettings.manipulateBlockLimit, messageKey, messageExtraArgsGetter);
	}

	@FunctionalInterface
	public interface ExecuteImpl
	{
		void executeAt(CommandContext<CommandSourceStack> ctx, BlockPos blockPos);
	}

	@FunctionalInterface
	public interface MessageExtraArgsGetter
	{
		MessageExtraArgsGetter NONE = ctx -> new Object[0];

		Object[] get(CommandContext<CommandSourceStack> ctx);
	}

	public int process(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
	{
		BlockPos from = getLoadedBlockPos(ctx, "from");
		BlockPos to = getLoadedBlockPos(ctx, "to");
		BoundingBox box = PositionUtils.createBlockBox(from, to);
		long blockCount = (long)box.getXSpan() * box.getXSpan() * box.getZSpan();
		if (blockCount > this.limit)
		{
			throw FillCommandAccessor.getTooBigException().create(this.limit, blockCount);
		}

		BlockPos.betweenClosedStream(from, to).forEach(blockPos -> this.impl.executeAt(ctx, blockPos));

		String hoverText = TextUtils.coord(from) + " -> " + TextUtils.coord(to);
		List<Object> trArgs = Lists.newArrayList();
		trArgs.add(blockCount);
		Collections.addAll(trArgs, this.messageExtraArgsGetter.get(ctx));
		Messenger.tell(ctx.getSource(), Messenger.fancy(
				tr(this.messageKey, trArgs.toArray()),
				Messenger.s(hoverText),
				Messenger.ClickEvents.suggestCommand(hoverText)
		));

		return (int)blockCount;
	}
}
