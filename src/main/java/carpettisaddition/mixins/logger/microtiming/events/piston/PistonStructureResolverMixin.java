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

package carpettisaddition.mixins.logger.microtiming.events.piston;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.IPistonStructureResolver;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public abstract class PistonStructureResolverMixin implements IPistonStructureResolver
{
	@Shadow @Final
	private BlockPos startPos;

	@Unique
	private final boolean microtimingRecordingEnabled$TISCM = MicroTimingLoggerManager.isLoggerActivated();

	@Unique @Nullable
	private FailureReason failureReason$TISCM = null;

	@Unique @Nullable
	private BlockPos failurePos$TISCM = null;

	@Override
	@Nullable
	public FailureReason getFailureReason$TISCM()
	{
		return this.failureReason$TISCM;
	}

	@Override
	@Nullable
	public BlockPos getFailurePos$TISCM()
	{
		return this.failurePos$TISCM;
	}

	@Inject(method = "resolve", at = @At("HEAD"))
	private void microtimingLogger_resetRecording(CallbackInfoReturnable<Boolean> cir)
	{
		this.failureReason$TISCM = null;
		this.failurePos$TISCM = null;
	}

	@ModifyExpressionValue(
			method = "addBlockLine",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;I)Lnet/minecraft/core/BlockPos;",
					ordinal = 0
			)
	)
	private BlockPos microtimingLogger_recordBackwardBlockPos(BlockPos backwardPos, @Share("backwardPos") LocalRef<BlockPos> backwardPosStore)
	{
		if (this.microtimingRecordingEnabled$TISCM)
		{
			backwardPosStore.set(backwardPos);
		}
		return backwardPos;
	}

	@ModifyExpressionValue(
			method = "addBlockLine",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;I)Lnet/minecraft/core/BlockPos;",
					ordinal = 2
			)
	)
	private BlockPos microtimingLogger_recordForwardBlockPos(BlockPos forwardPos, @Share("forwardPos") LocalRef<BlockPos> forwardPosStore)
	{
		if (this.microtimingRecordingEnabled$TISCM)
		{
			forwardPosStore.set(forwardPos);
		}
		return forwardPos;
	}

	@Inject(
			method = "resolve",
			at = @At(
					value = "RETURN",
					ordinal = 1
			)
	)
	private void microtimingLogger_recordImmovable0(CallbackInfoReturnable<Boolean> cir)
	{
		if (this.microtimingRecordingEnabled$TISCM)
		{
			this.failureReason$TISCM = FailureReason.GOT_IMMOVABLE_BLOCK;
			this.failurePos$TISCM = this.startPos;
		}
	}

	@Inject(
			method = "addBlockLine",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isPushable(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;ZLnet/minecraft/core/Direction;)Z",
							ordinal = 2
					)
			),
			at = @At(
					value = "RETURN",
					ordinal = 0
			)
	)
	private void microtimingLogger_recordImmovable1(CallbackInfoReturnable<Boolean> cir, @Share("forwardPos") LocalRef<BlockPos> forwardPosStore)
	{
		if (this.microtimingRecordingEnabled$TISCM && forwardPosStore.get() != null)
		{
			this.failureReason$TISCM = FailureReason.GOT_IMMOVABLE_BLOCK;
			this.failurePos$TISCM = forwardPosStore.get();
		}
	}

	@ModifyVariable(
			method = "addBlockLine",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=12",
							ordinal = 0
					)
			),
			at = @At(
					value = "RETURN",
					ordinal = 0
			),
			argsOnly = true
	)
	private BlockPos microtimingLogger_recordPushLimitExceeded0(BlockPos blockPos)
	{
		if (this.microtimingRecordingEnabled$TISCM)
		{
			this.failureReason$TISCM = FailureReason.PUSH_LIMIT_EXCEEDED;
			this.failurePos$TISCM = blockPos;
		}
		return blockPos;
	}

	@Inject(
			method = "addBlockLine",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=12",
							ordinal = 1
					)
			),
			at = @At(
					value = "RETURN",
					ordinal = 0
			)
	)
	private void microtimingLogger_recordPushLimitExceeded1(CallbackInfoReturnable<Boolean> cir, @Share("backwardPos") LocalRef<BlockPos> backwardPosStore)
	{
		if (this.microtimingRecordingEnabled$TISCM && backwardPosStore.get() != null)
		{
			this.failureReason$TISCM = FailureReason.PUSH_LIMIT_EXCEEDED;
			this.failurePos$TISCM = backwardPosStore.get();
		}
	}

	@Inject(
			method = "addBlockLine",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=12",
							ordinal = 2
					)
			),
			at = @At(
					value = "RETURN",
					ordinal = 0
			)
	)
	private void microtimingLogger_recordPushLimitExceeded2(CallbackInfoReturnable<Boolean> cir, @Share("forwardPos") LocalRef<BlockPos> forwardPosStore)
	{
		if (this.microtimingRecordingEnabled$TISCM && forwardPosStore.get() != null)
		{
			this.failureReason$TISCM = FailureReason.PUSH_LIMIT_EXCEEDED;
			this.failurePos$TISCM = forwardPosStore.get();
		}
	}
}
