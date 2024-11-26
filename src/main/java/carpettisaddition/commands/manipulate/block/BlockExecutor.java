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
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;

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
		void executeAt(CommandContext<ServerCommandSource> ctx, BlockPos blockPos);
	}

	@FunctionalInterface
	public interface MessageExtraArgsGetter
	{
		MessageExtraArgsGetter NONE = ctx -> new Object[0];

		Object[] get(CommandContext<ServerCommandSource> ctx);
	}

	public int process(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException
	{
		BlockPos from = getLoadedBlockPos(ctx, "from");
		BlockPos to = getLoadedBlockPos(ctx, "to");
		BlockBox box = PositionUtils.createBlockBox(from, to);
		long blockCount = (long)box.getBlockCountX() * box.getBlockCountX() * box.getBlockCountZ();
		if (blockCount > this.limit)
		{
			throw FillCommandAccessor.getTooBigException().create(this.limit, blockCount);
		}

		BlockPos.stream(from, to).forEach(blockPos -> this.impl.executeAt(ctx, blockPos));

		String hoverText = TextUtil.coord(from) + " -> " + TextUtil.coord(to);
		List<Object> trArgs = Lists.newArrayList();
		trArgs.add(blockCount);
		Collections.addAll(trArgs, this.messageExtraArgsGetter.get(ctx));
		Messenger.tell(ctx.getSource(), Messenger.fancy(
				tr(this.messageKey, trArgs.toArray()),
				Messenger.s(hoverText),
				new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, hoverText)
		));

		return (int)blockCount;
	}
}
