package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.block.ObserverBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

public abstract class ComponentPoweredMixins
{
	@Mixin(ComparatorBlock.class)
	public static class ComparatorBlockMixin
	{
		@Inject(
				method = "update",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
						ordinal = 0
				)
		)
		private void onComponentUnpowered(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, false);
		}

		@Inject(
				method = "update",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
						ordinal = 1
				)
		)
		private void onComponentPowered(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, true);
		}
	}

	@Mixin({AbstractRedstoneGateBlock.class, ObserverBlock.class})
	public static abstract class AbstractRedstoneGateBlockAndObserverBlockMixin
	{
		@Inject(
				method = "scheduledTick",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
						ordinal = 0
				)
		)
		private void onComponentUnpowered(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, false);
		}

		@Inject(
				method = "scheduledTick",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
						ordinal = 1
				)
		)
		private void onComponentPowered(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, true);
		}
	}
}
