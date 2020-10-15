package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

// This class is for minimum TileTick Event scheduling listening if any mod replace the container of ServerTickScheduler
// for example, lithium mod
public abstract class ScheduleTileTickEventMixins
{
	@Mixin(ComparatorBlock.class)
	public static class ComparatorBlockMixin
	{
		@Inject(
				method = "updatePowered",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;ILnet/minecraft/world/TickPriority;)V"
				),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void onScheduleTileTickEvent(World world, BlockPos pos, BlockState state, CallbackInfo ci, TickPriority tickPriority)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (ComparatorBlock) (Object) this, pos, 2, tickPriority);
		}
	}

	@Mixin(AbstractRedstoneGateBlock.class)
	public static abstract class AbstractRedstoneGateBlockMixin
	{
		@Shadow protected abstract int getUpdateDelayInternal(BlockState state);

		@Inject(
				method = "scheduledTick",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/server/world/ServerTickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;ILnet/minecraft/world/TickPriority;)V"
				)
		)
		private void onScheduleTileTickEvent(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (AbstractRedstoneGateBlock)(Object)this, pos, this.getUpdateDelayInternal(state), TickPriority.VERY_HIGH);
		}

		@Inject(
				method = "updatePowered",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;ILnet/minecraft/world/TickPriority;)V"
				),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void onScheduleTileTickEvent(World world, BlockPos pos, BlockState state, CallbackInfo ci, boolean bl, boolean bl2, TickPriority tickPriority)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (AbstractRedstoneGateBlock)(Object)this, pos, this.getUpdateDelayInternal(state), tickPriority);
		}

		@Inject(
				method = "onPlaced",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (AbstractRedstoneGateBlock)(Object)this, pos, 1);
		}
	}

	@Mixin(ObserverBlock.class)
	public static abstract class ObserverBlockMixin
	{
		@Inject(
				method = "scheduledTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/server/world/ServerTickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (ObserverBlock)(Object)this, pos, 2);
		}

		@Inject(
				method = "scheduleTick(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)V",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(IWorld world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world.getWorld(), (ObserverBlock)(Object)this, pos, 2);
		}
	}

	@Mixin(RedstoneTorchBlock.class)
	public static abstract class RedstoneTorchBlockMixin
	{
		@Shadow public abstract int getTickRate(WorldView worldView);

		@Inject(
				method = "update",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private static void onScheduleTileTickEvent(BlockState state, World world, BlockPos pos, Random random, boolean unpower, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, world.getBlockState(pos).getBlock(), pos, 160);
		}

		@Inject(
				method = "neighborUpdate",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (RedstoneTorchBlock)(Object)this, pos, this.getTickRate(world));
		}
	}

	@Mixin(AbstractButtonBlock.class)
	public static abstract class AbstractButtonBlockMixin
	{
		@Shadow public abstract int getTickRate(WorldView worldView);

		@Inject(
				method = {"method_21845", "tryPowerWithProjectiles"},
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(BlockState state, World world, BlockPos pos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (AbstractButtonBlock)(Object)this, pos, this.getTickRate(world));
		}
	}

	@Mixin(AbstractPressurePlateBlock.class)
	public static abstract class AbstractPressurePlateBlockMixin
	{
		@Shadow public abstract int getTickRate(WorldView worldView);

		@Inject(
				method = "updatePlateState",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(World world, BlockPos pos, BlockState blockState, int rsOut, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (AbstractPressurePlateBlock)(Object)this, pos, this.getTickRate(world));
		}
	}

	@Mixin(TripwireHookBlock.class)
	public static abstract class TripwireHookBlockMixin extends Block
	{
		public TripwireHookBlockMixin(Settings settings)
		{
			super(settings);
		}

		@Inject(
				method = "update",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/TickScheduler;schedule(Lnet/minecraft/util/math/BlockPos;Ljava/lang/Object;I)V"
				)
		)
		private void onScheduleTileTickEvent(World world, BlockPos pos, BlockState state, boolean beingRemoved, boolean bl, int i, BlockState blockState, CallbackInfo ci)
		{
			MicroTickLoggerManager.onScheduleTileTickEvent(world, (TripwireHookBlock)(Object)this, pos, this.getTickRate(world));
		}
	}
}
