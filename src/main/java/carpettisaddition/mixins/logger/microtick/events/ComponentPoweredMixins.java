package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import net.minecraft.block.*;
import net.minecraft.block.enums.ComparatorMode;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

public abstract class ComponentPoweredMixins
{
	@Mixin(ComparatorBlock.class)
	public static abstract class ComparatorBlockMixin extends AbstractRedstoneGateBlock
	{
		protected ComparatorBlockMixin(Settings settings)
		{
			super(settings);
		}

		@Shadow protected abstract int getPower(World world, BlockPos pos, BlockState state);

		@Shadow @Final public static EnumProperty<ComparatorMode> MODE;

		@Shadow protected abstract int calculateOutputSignal(World world, BlockPos pos, BlockState state);

		@Inject(
				method = "update",
				at = @At(
						value = "INVOKE",
						target = "Lnet/minecraft/block/ComparatorBlock;updateTarget(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V",
						ordinal = 0
				)
		)
		private void onComponentPowered(World world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			if (MicroTickLoggerManager.isLoggerActivated())
			{
				int rawOutput = this.calculateOutputSignal(world, pos, state);
				int output = this.hasPower(world, pos, state) ? rawOutput : 0;
				MicroTickLoggerManager.onComponentPowered(world, pos, output);
			}
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

	@Mixin(AbstractPressurePlateBlock.class)
	public static abstract class AbstractPressurePlateBlockMixin
	{
		@Inject(
				method = "updatePlateState",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void onComponentPowered(World world, BlockPos pos, BlockState blockState, int rsOut, CallbackInfo ci, int i)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, i);
		}
	}

	@Mixin(AbstractButtonBlock.class)
	public static abstract class AbstractButtonBlockMixin
	{
		@Inject(
				method = {"method_21845", "tryPowerWithProjectiles"},
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				)
		)
		private void onComponentPowered(BlockState blockState, World world, BlockPos blockPos, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, blockPos, true);
		}

		@Inject(
				method = "scheduledTick",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				)
		)
		private void onComponentPowered(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, false);
		}
	}

	@Mixin(LeverBlock.class)
	public static abstract class LeverBlockMixin
	{
		@Shadow @Final public static BooleanProperty POWERED;

		@Inject(
				method = "method_21846",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				)
		)
		private void onComponentPowered(BlockState blockState, World world, BlockPos blockPos, CallbackInfoReturnable<BlockState> cir)
		{
			MicroTickLoggerManager.onComponentPowered(world, blockPos, blockState.get(POWERED));
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
				),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void onComponentPowered(BlockState state, World world, BlockPos pos, Block arg3, CallbackInfo ci, boolean bl, boolean bl2)
		{
			MicroTickLoggerManager.onComponentPowered(world, pos, bl2);
		}
	}

	@Mixin(TripwireHookBlock.class)
	public static abstract class TripwireHookBlockMixin
	{
		@Shadow @Final public static BooleanProperty POWERED;
		private final ThreadLocal<Boolean> previousPowerState = new ThreadLocal<>();

		@Inject(method = "update", at = @At("HEAD"))
		private void onUpdate(World world, BlockPos pos, BlockState state, boolean beingRemoved, boolean bl, int i, BlockState blockState, CallbackInfo ci)
		{
			previousPowerState.set(state.get(POWERED));
		}

		@Inject(
				method = "update",
				slice = @Slice(
						to = @At(
								value = "INVOKE",
								target = "Lnet/minecraft/block/TripwireHookBlock;updateNeighborsOnAxis(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V",
								ordinal = 1
						)
				),
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
				),
				require = 2,
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void onComponentPowered(World world, BlockPos pos, BlockState state, boolean beingRemoved, boolean bl, int i, BlockState blockState, CallbackInfo ci, Direction direction, boolean bl2, boolean bl3, boolean bl4, boolean bl5, int j, BlockState blockStates[], BlockState blockState3)
		{
			if (MicroTickLoggerManager.isLoggerActivated())
			{
				boolean current = previousPowerState.get();
				if (current != blockState3.get(POWERED))
				{
					MicroTickLoggerManager.onComponentPowered(world, pos, current);
				}
			}
		}
	}
}
