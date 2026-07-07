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
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.LongArgumentType.getLong;
import static com.mojang.brigadier.arguments.LongArgumentType.longArg;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ChunkManipulator extends AbstractManipulator
{
	public ChunkManipulator()
	{
		super("chunk");
	}

	// ============================== Command ==============================

	@FunctionalInterface
	private interface BatchOperationTreeBuilder
	{
		ArgumentBuilder<CommandSourceStack, ?> build(ArgumentBuilder<CommandSourceStack, ?> node);
	}

	@FunctionalInterface
	private interface CustomBatchOperationTreeBuilder
	{
		void buildWith(LiteralArgumentBuilder<CommandSourceStack> subcommandNode, BatchOperationTreeBuilder builder);
	}

	@FunctionalInterface
	private interface RadiusNodeMakerOperationFunc
	{
		int run(CommandContext<CommandSourceStack> ctx, int radius) throws CommandSyntaxException;
	}

	@FunctionalInterface
	private interface RadiusNodeMaker
	{
		LiteralArgumentBuilder<CommandSourceStack> make(String name, RadiusNodeMakerOperationFunc func);
	}

	@SuppressWarnings("SameParameterValue")
	private LiteralArgumentBuilder<CommandSourceStack> makeBatchOperationNode(Operator operator, String subcommand, int maxRangeRadius, @Nullable CustomBatchOperationTreeBuilder deco)
	{
		RadiusNodeMaker makeRadiusNode = (name, func) ->
				literal(name).
						then(argument("radius", integer(0, maxRangeRadius)).
								executes(c -> func.run(c, getInteger(c, "radius")))
						);

		if (deco == null)
		{
			deco = (scn, builder) -> builder.build(scn);
		}

		LiteralArgumentBuilder<CommandSourceStack> subcommandNode = literal(subcommand);
		deco.buildWith(subcommandNode, decoNode -> {
			decoNode.executes(c -> {
						Messenger.tell(c.getSource(), Messenger.formatting(tr("help.danger_notice"), ChatFormatting.RED));
						Messenger.tell(c.getSource(), tr("help.root"));
						return 0;
					}).
					then(literal("current").
							executes(operator::operateCurrent)
					).
					then(makeRadiusNode.make("square", operator::operateInSquare)).
					then(makeRadiusNode.make("chebyshev", operator::operateInSquare)).
					then(makeRadiusNode.make("circle", operator::operateInCircle)).
					then(makeRadiusNode.make("euclidean", operator::operateInCircle)).
					then(literal("at").
							then(argument("chunkX", integer()).
									then(argument("chunkZ", integer()).
											executes(c -> operator.operateAt(
													c, new ChunkPos(getInteger(c, "chunkX"), getInteger(c, "chunkZ"))
											))
									)
							)
					);
			return decoNode;
		});

		return subcommandNode;
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		ThrottledOperator eraseOperator = new ThrottledOperator(this, this::doEraseChunks);
		ThrottledOperator relightOperator = new ThrottledOperator(this, this::doRelightChunks);
		SimpleOperator inhabitedTimeOperator = new SimpleOperator(this, this::doUpdateInhabitedTime);

		// TODO copy "relight" to "block manipulator" for block box impl
		context.node.
				then(makeBatchOperationNode(eraseOperator, "erase", 32, null)).
				then(makeBatchOperationNode(relightOperator, "relight", 32, null).
						then(literal("abort").executes(c -> relightOperator.abort(c.getSource())))
				).
				then(makeBatchOperationNode(inhabitedTimeOperator, "inhabitedTime", 32, (scn, builder) -> {
					scn.then(builder.build(literal("query")));
					scn.then(literal("set").then(
							builder.build(argument("ticks", longArg(0)))
					));
					scn.then(literal("add").then(
							builder.build(argument("deltaTicks", longArg()))
					));
				}));
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

	@SuppressWarnings("OptionalIsPresent")
	private int doUpdateInhabitedTime(CommandContext<CommandSourceStack> ctx, List<ChunkPos> chunkPosList) throws CommandSyntaxException
	{
		ChunkInhabitedTimeUpdater updater = new ChunkInhabitedTimeUpdater(this.getTranslator().getDerivedTranslator("inhabitedTime"), chunkPosList, ctx.getSource());

		Optional<Long> setTicks = CommandUtils.getOptArg(() -> getLong(ctx, "ticks"));
		if (setTicks.isPresent())
		{
			return updater.set(setTicks.get());
		}

		Optional<Long> addDeltaTicks = CommandUtils.getOptArg(() -> getLong(ctx, "deltaTicks"));
		if (addDeltaTicks.isPresent())
		{
			return updater.add(addDeltaTicks.get());
		}

		return updater.query();
	}
}
