package carpettisaddition.mixins.rule.instantCommandBlock;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.instantCommandBlock.ICommandBlockExecutor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin
{
	@Shadow public abstract void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random);

	@Inject(
			method = "neighborUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;method_39279(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;I)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void justExecuteRightNow(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved, CallbackInfo ci, BlockEntity blockEntity, CommandBlockBlockEntity commandBlockBlockEntity)
	{
		if (CarpetTISAdditionSettings.instantCommandBlock)
		{
			if (world instanceof ServerWorld && commandBlockBlockEntity.getCommandBlockType() == CommandBlockBlockEntity.Type.REDSTONE)
			{
				ServerWorld serverWorld = (ServerWorld)world;
				Block blockBelow = world.getBlockState(pos.down()).getBlock();
				if (blockBelow == Blocks.REDSTONE_ORE)
				{
					ICommandBlockExecutor icbe = (ICommandBlockExecutor)commandBlockBlockEntity.getCommandExecutor();
					icbe.setIgnoreWorldTimeCheck(true);
					this.scheduledTick(state, serverWorld, pos, serverWorld.getRandom());
					icbe.setIgnoreWorldTimeCheck(false);
					ci.cancel();
				}
			}
		}
	}
}
