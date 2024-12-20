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
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.ChunkPos;

import java.util.List;
import java.util.function.BiFunction;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ChunkManipulator extends AbstractManipulator
{
	public ChunkManipulator()
	{
		super("chunk");
	}

	// ============================== Command ==============================

	private LiteralArgumentBuilder<ServerCommandSource> makeBatchOperationNode(String subcommand, int maxRangeRadius, ThrottledOperator.OperateImpl operateImpl)
	{
		ThrottledOperator operator = new ThrottledOperator(this, operateImpl);

		BiFunction<String, BiFunction<ServerCommandSource, Integer, Integer>, LiteralArgumentBuilder<ServerCommandSource>> makeRadiusNode = (name, func) ->
				literal(name).
						then(argument("radius", integer(0, maxRangeRadius)).
								executes(c -> func.apply(c.getSource(), getInteger(c, "radius")))
						);

		return literal(subcommand).
				executes(c -> operator.operateCurrent(c.getSource())).
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
		// TODO copy relight to block manipulator for block box impl
		context.node.
				then(makeBatchOperationNode("erase", 32, this::doEraseChunks)).
				then(makeBatchOperationNode("relight", 2, this::doRelightChunks));
	}

	// ============================== Impl ==============================

	private int doEraseChunks(ServerCommandSource source, List<ChunkPos> chunkPosList, Runnable doneCallback)
	{
		ChunkEraser chunkEraser = new ChunkEraser(this.getTranslator().getDerivedTranslator("erase"), chunkPosList, source);
		chunkEraser.erase().thenRun(doneCallback);
		return chunkPosList.size();
	}

	private int doRelightChunks(ServerCommandSource source, List<ChunkPos> chunkPosList, Runnable doneCallback)
	{
		ChunkRelighter chunkRelighter = new ChunkRelighter(this.getTranslator().getDerivedTranslator("relight"), chunkPosList, source);
		chunkRelighter.relight().thenRun(doneCallback);
		return chunkPosList.size();
	}
}
