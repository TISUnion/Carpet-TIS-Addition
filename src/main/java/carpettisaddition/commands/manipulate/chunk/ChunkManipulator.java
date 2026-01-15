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

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.ChunkPos;

import java.util.List;
import java.util.function.BiFunction;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ChunkManipulator extends AbstractManipulator
{
	public ChunkManipulator()
	{
		super("chunk");
	}

	// ============================== Command ==============================

	@SuppressWarnings("SameParameterValue")
	private LiteralArgumentBuilder<CommandSourceStack> makeBatchOperationNode(ThrottledOperator operator, String subcommand, int maxRangeRadius)
	{
		BiFunction<String, BiFunction<CommandSourceStack, Integer, Integer>, LiteralArgumentBuilder<CommandSourceStack>> makeRadiusNode = (name, func) ->
				literal(name).
						then(argument("radius", integer(0, maxRangeRadius)).
								executes(c -> func.apply(c.getSource(), getInteger(c, "radius")))
						);

		return literal(subcommand).
				executes(c -> {
					Messenger.tell(c.getSource(), Messenger.formatting(tr("help.danger_notice"), ChatFormatting.RED));
					Messenger.tell(c.getSource(), tr("help.root"));
					return 0;
				}).
				then(literal("current").
						executes(c -> operator.operateCurrent(c.getSource()))
				).
				then(makeRadiusNode.apply("square", operator::operateInSquare)).
				then(makeRadiusNode.apply("chebyshev", operator::operateInSquare)).
				then(makeRadiusNode.apply("circle", operator::operateInCircle)).
				then(makeRadiusNode.apply("euclidean", operator::operateInCircle)).
				then(literal("at").
						then(argument("chunkX", integer()).
								then(argument("chunkZ", integer()).
										executes(c -> operator.operateAt(
												c.getSource(), new ChunkPos(getInteger(c, "chunkX"), getInteger(c, "chunkZ"))
										))
								)
						)
				);
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		ThrottledOperator eraseOperator = new ThrottledOperator(this, this::doEraseChunks);
		ThrottledOperator relightOperator = new ThrottledOperator(this, this::doRelightChunks);

		// TODO copy "relight" to "block manipulator" for block box impl
		context.node.
				then(makeBatchOperationNode(eraseOperator, "erase", 32)).
				then(makeBatchOperationNode(relightOperator, "relight", 32).
						then(literal("abort").executes(c -> relightOperator.abort(c.getSource())))
				);
	}

	// ============================== Impl ==============================

	private AbortableOperation doEraseChunks(CommandSourceStack source, List<ChunkPos> chunkPosList, Runnable doneCallback)
	{
		ChunkEraser chunkEraser = new ChunkEraser(this.getTranslator().getDerivedTranslator("erase"), chunkPosList, source);
		chunkEraser.erase().thenRun(doneCallback);
		return chunkEraser;
	}

	private AbortableOperation doRelightChunks(CommandSourceStack source, List<ChunkPos> chunkPosList, Runnable doneCallback)
	{
		ChunkRelighter chunkRelighter = new ChunkRelighter(this.getTranslator().getDerivedTranslator("relight"), chunkPosList, source);
		chunkRelighter.relight().thenRun(doneCallback);
		return chunkRelighter;
	}
}
