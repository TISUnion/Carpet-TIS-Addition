package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

public abstract class EmitBlockUpdateMixins
{
	@Mixin(AbstractRedstoneGateBlock.class)
	public static abstract class AbstractRedstoneGateBlockMixin
	{
		@Inject(method = "updateTarget", at = @At("HEAD"))
		private void startEmitBlockUpdate(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractRedstoneGateBlock)(Object)this, pos, EventType.ACTION_START, "updateTarget");
		}

		@Inject(method = "updateTarget", at = @At("RETURN"))
		private void endEmitBlockUpdate(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractRedstoneGateBlock)(Object)this, pos, EventType.ACTION_END, "updateTarget");
		}
	}

	@Mixin(ObserverBlock.class)
	public static abstract class ObserverBlockMixin
	{
		@Inject(method = "updateNeighbors", at = @At("HEAD"))
		private void startEmitBlockUpdate(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (ObserverBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbors", at = @At("RETURN"))
		private void endEmitBlockUpdate(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (ObserverBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(PoweredRailBlock.class)
	public static abstract class PoweredRailBlockMixin
	{
		@Inject(
				method = "updateBlockState",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				)
		)
		private void startEmitBlockUpdate(BlockState state, World world, BlockPos pos, Block neighbor, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (PoweredRailBlock)(Object)this, pos, EventType.ACTION_START, "updateBlockState");
		}

		@Inject(
				method = "updateBlockState",
				at = @At("RETURN"),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void endEmitBlockUpdate(BlockState state, World world, BlockPos pos, Block neighbor, CallbackInfo ci, boolean bl, boolean bl2)
		{
			if (bl2 != bl)  // vanilla copy, if power state should be changed
			{
				MicroTickLoggerManager.onEmitBlockUpdate(world, (PoweredRailBlock) (Object) this, pos, EventType.ACTION_END, "updateBlockState");
			}
		}
	}

	@Mixin(AbstractButtonBlock.class)
	public static abstract class AbstractButtonBlockMixin
	{
		@Inject(method = "updateNeighbors", at = @At("HEAD"))
		private void startEmitBlockUpdate(BlockState state, World world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractButtonBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbors", at = @At("RETURN"))
		private void endEmitBlockUpdate(BlockState state, World world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractButtonBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(AbstractPressurePlateBlock.class)
	public static abstract class AbstractPressurePlateBlockMixin
	{
		@Inject(method = "updateNeighbors", at = @At("HEAD"))
		private void startEmitBlockUpdate(World world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractPressurePlateBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbors", at = @At("RETURN"))
		private void endEmitBlockUpdate(World world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (AbstractPressurePlateBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(TripwireHookBlock.class)
	public static abstract class TripwireHookBlockMixin
	{
		@Inject(method = "updateNeighborsOnAxis", at = @At("HEAD"))
		private void startEmitBlockUpdate(World world, BlockPos pos, Direction direction, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (TripwireHookBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighborsOnAxis");
		}

		@Inject(method = "updateNeighborsOnAxis", at = @At("RETURN"))
		private void endEmitBlockUpdate(World world, BlockPos pos, Direction direction, CallbackInfo ci)
		{
			MicroTickLoggerManager.onEmitBlockUpdate(world, (TripwireHookBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighborsOnAxis");
		}
	}
}
