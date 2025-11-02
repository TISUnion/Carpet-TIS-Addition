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
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.server.commands.FillCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.function.Predicate;

import static net.minecraft.commands.arguments.coordinates.BlockPosArgument.getLoadedBlockPos;
import static net.minecraft.commands.arguments.blocks.BlockPredicateArgument.blockPredicate;
import static net.minecraft.commands.arguments.blocks.BlockPredicateArgument.getBlockPredicate;
import static net.minecraft.commands.arguments.blocks.BlockStateArgument.getBlock;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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

	private int softReplace(CommandContext<CommandSourceStack> context) throws CommandSyntaxException
	{
		try
		{
			FillModeEnhanceContext.isSoftReplace.set(true);
			Predicate<BlockInWorld> filter;
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
					//$$ BoundingBox.fromCorners
					//#else
					new BoundingBox
					//#endif
							(getLoadedBlockPos(context, "from"), getLoadedBlockPos(context, "to")),
					getBlock(context, "block"),
					FillCommand.Mode.REPLACE,
					filter
					//#if MC >= 12105
					//$$ , false
					//#endif
			);
		}
		finally
		{
			FillModeEnhanceContext.isSoftReplace.set(false);
		}
	}
}
