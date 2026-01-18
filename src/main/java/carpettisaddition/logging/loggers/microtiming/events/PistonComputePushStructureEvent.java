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

package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.interfaces.IPistonStructureResolver;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.logger.microtiming.events.piston.PistonStructureResolverAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.TextUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PistonComputePushStructureEvent extends BaseEvent
{
	private final DimensionWrapper dimension;
	private final Result result;

	public PistonComputePushStructureEvent(DimensionWrapper dimension, Block block, Result result)
	{
		super(EventType.EVENT, "piston_compute_push_structure", block);
		this.dimension = dimension;
		this.result = result;
	}

	private BaseComponent createBlockOrderText(BaseComponent title, List<Pair<BlockPos, BlockState>> blocks)
	{
		List<BaseComponent> lines = Lists.newArrayList();
		lines.add(title);
		for (int i = 0; i < blocks.size(); i++)
		{
			lines.add(Messenger.c(
					Messenger.s(String.format("%s. ", i + 1), ChatFormatting.GRAY),
					Messenger.coord(blocks.get(i).getFirst()),
					Messenger.s(" "),
					Messenger.block(blocks.get(i).getSecond().getBlock())
			));
		}
		return Messenger.join(Messenger.newLine(),lines);
	}

	private BaseComponent createPushInfoText()
	{
		return Messenger.join(
				Messenger.newLine(),
				tr("info.piston_pos", Messenger.coord(this.result.pistonPos)),
				tr("info.piston_direction", MicroTimingUtil.getFormattedDirectionText(this.result.pistonDirection)),
				tr("info.push_type", tr(this.result.extending ? "push" : "pull")),
				tr("info.push_direction", MicroTimingUtil.getFormattedDirectionText(this.result.pushDirection)),
				tr("info.push_start_pos", Messenger.coord(this.result.startPos))
		);
	}

	@Override
	public BaseComponent toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseComponent pushInfoText = this.createPushInfoText();
		list.add(Messenger.fancy(
				Messenger.c(
						Messenger.formatting(tr("computed"), COLOR_ACTION),
						Messenger.s(": ", ChatFormatting.GRAY),
						Messenger.formatting(tr(this.result.extending ? "push" : "pull"), COLOR_RESULT)
				),
				pushInfoText,
				//#if MC >= 1.15
				Messenger.ClickEvents.copyToClipBoard(pushInfoText.getString())
				//#else
				//$$ null
				//#endif
		));
		list.add(Messenger.s(" "));
		if (this.result.success)
		{
			BaseComponent toPushText = this.createBlockOrderText(tr("to_push", this.result.toPush.size()), this.result.toPush);
			BaseComponent toDestroyText = this.createBlockOrderText(tr("to_destroy", this.result.toDestroy.size()), this.result.toDestroy);
			list.add(Messenger.fancy(
					Messenger.s("*" + this.result.toPush.size(), ChatFormatting.GOLD),
					toPushText,
					//#if MC >= 1.15
					Messenger.ClickEvents.copyToClipBoard(toPushText.getString())
					//#else
					//$$ null
					//#endif
			));
			list.add(Messenger.s(" "));
			list.add(Messenger.fancy(
					Messenger.s("-" + this.result.toDestroy.size(), ChatFormatting.GOLD),
					toDestroyText,
					//#if MC >= 1.15
					Messenger.ClickEvents.copyToClipBoard(toDestroyText.getString())
					//#else
					//$$ null
					//#endif
			));
			list.add(Messenger.s(" "));
		}

		BaseComponent successText = MicroTimingUtil.getSuccessText(this.result.success, false);
		if (!this.result.success && this.result.failureReason != null && this.result.failureBlock != null)
		{
			successText = Messenger.fancy(
					successText,
					Messenger.join(
							Messenger.newLine(),
							Messenger.c(
									Messenger.formatting(MicroTimingUtil.getUtilTranslator().tr("failed"), ChatFormatting.RED),
									Messenger.s(": ", ChatFormatting.GRAY),
									tr("failure_reason." + this.result.failureReason.name().toLowerCase())
							),
							tr("failure_pos", Messenger.coord(this.result.failureBlock.getFirst())),
							tr("failure_block", Messenger.block(this.result.failureBlock.getSecond().getBlock()))
					),
					Messenger.ClickEvents.suggestCommand(TextUtils.tp(this.result.failureBlock.getFirst(), this.dimension))
			);
		}
		list.add(successText);
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		PistonComputePushStructureEvent that = (PistonComputePushStructureEvent)o;
		return Objects.equals(dimension, that.dimension) && Objects.equals(result, that.result);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), dimension, result);
	}

	public static class Result
	{
		public final boolean success;
		public final List<Pair<BlockPos, BlockState>> toPush;
		public final List<Pair<BlockPos, BlockState>> toDestroy;
		public final BlockPos pistonPos;
		public final Direction pistonDirection;
		public final boolean extending;
		public final Direction pushDirection;
		public final BlockPos startPos;
		public final @Nullable IPistonStructureResolver.FailureReason failureReason;
		public final @Nullable Pair<BlockPos, BlockState> failureBlock;
		private final int hashCode;

		public Result(
				boolean success, List<Pair<BlockPos, BlockState>> toPush, List<Pair<BlockPos, BlockState>> toDestroy,
				BlockPos pistonPos, Direction pistonDirection, boolean extending, Direction pushDirection, BlockPos startPos,
				@Nullable IPistonStructureResolver.FailureReason failureReason, @Nullable Pair<BlockPos, BlockState> failureBlock)
		{
			this.success = success;
			this.toPush = toPush;
			this.toDestroy = toDestroy;
			this.pistonPos = pistonPos;
			this.pistonDirection = pistonDirection;
			this.extending = extending;
			this.pushDirection = pushDirection;
			this.startPos = startPos;
			this.failureReason = failureReason;
			this.failureBlock = failureBlock;
			this.hashCode = this.computeHashCode();
		}

		public static Result from(boolean success, PistonStructureResolver resolver)
		{
			PistonStructureResolverAccessor accessor = (PistonStructureResolverAccessor)resolver;
			Level world = accessor.getLevel$TISCM();
			List<Pair<BlockPos, BlockState>> toPush = Lists.newArrayList();
			List<Pair<BlockPos, BlockState>> toDestroy = Lists.newArrayList();
			for (BlockPos pos : resolver.getToPush())
			{
				toPush.add(Pair.of(pos, world.getBlockState(pos)));
			}
			for (BlockPos pos : resolver.getToDestroy())
			{
				toDestroy.add(Pair.of(pos, world.getBlockState(pos)));
			}
			IPistonStructureResolver iResolver = (IPistonStructureResolver)resolver;
			return new Result(
					success, toPush, toDestroy,
					accessor.getPistonPos$TISCM(),
					accessor.getPistonDirection$TISCM(),
					accessor.getExtending$TISCM(),
					accessor.getPushDirection$TISCM(),
					accessor.getStartPos$TISCM(),
					iResolver.getFailureReason$TISCM(),
					Optional.ofNullable(iResolver.getFailurePos$TISCM()).
							map(pos -> Pair.of(pos, world.getBlockState(pos))).
							orElse(null)
			);
		}

		@Override
		public boolean equals(Object o)
		{
			if (o == null || getClass() != o.getClass()) return false;
			Result result = (Result)o;
			return success == result.success && extending == result.extending && hashCode == result.hashCode && Objects.equals(toPush, result.toPush) && Objects.equals(toDestroy, result.toDestroy) && Objects.equals(pistonPos, result.pistonPos) && pistonDirection == result.pistonDirection && pushDirection == result.pushDirection && Objects.equals(startPos, result.startPos) && failureReason == result.failureReason && Objects.equals(failureBlock, result.failureBlock);
		}

		@Override
		public int hashCode()
		{
			return this.hashCode;
		}

		public int computeHashCode()
		{
			return Objects.hash(success, toPush, toDestroy, pistonPos, pistonDirection, extending, pushDirection, startPos, failureReason, failureBlock);
		}
	}
}
