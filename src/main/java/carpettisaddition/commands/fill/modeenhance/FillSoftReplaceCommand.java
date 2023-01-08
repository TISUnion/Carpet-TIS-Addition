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

package carpettisaddition.commands.fill.modeenhance;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.mixins.command.fill.modeenhance.FillCommandAccessor;
import carpettisaddition.utils.CarpetModUtil;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockBox;

import java.util.function.Predicate;

import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.arguments.BlockPredicateArgumentType.blockPredicate;
import static net.minecraft.command.arguments.BlockPredicateArgumentType.getBlockPredicate;
import static net.minecraft.command.arguments.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FillSoftReplaceCommand extends AbstractCommand implements CommandExtender
{
	private static final FillSoftReplaceCommand INSTANCE = new FillSoftReplaceCommand();

	private FillSoftReplaceCommand()
	{
		super("fill.softreplace");
	}

	public static FillSoftReplaceCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.then(literal("softreplace").
				requires(s -> CarpetModUtil.canUseCommand(s, CarpetTISAdditionSettings.fillCommandModeEnhance)).
				executes(this::softReplace).
				then(argument("filter", blockPredicate(
						//#if MC >= 11900
						//$$ context.commandBuildContext
						//#endif
						)).
						executes(this::softReplace)
				)
		);
	}

	private int softReplace(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		try
		{
			FillModeEnhanceContext.isSoftReplace.set(true);
			Predicate<CachedBlockPosition> filter;
			try
			{
				filter = getBlockPredicate(context, "filter");
			}
			catch (IllegalArgumentException e)  // arg not found
			{
				filter = null;
			}
			return FillCommandAccessor.invokeExecute(
					context.getSource(),
					//#if MC >= 11700
					//$$ BlockBox.create
					//#else
					new BlockBox
					//#endif
							(getLoadedBlockPos(context, "from"), getLoadedBlockPos(context, "to")),
					getBlockState(context, "block"),
					FillCommand.Mode.REPLACE,
					filter
			);
		}
		finally
		{
			FillModeEnhanceContext.isSoftReplace.set(false);
		}
	}
}
