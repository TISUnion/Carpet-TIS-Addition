package carpettisaddition.mixins.logger.turtleegg;

import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TurtleEggBlock.class)
public abstract class TurtleEggBlockMixin
{
	private final ThreadLocal<Entity> eggBreakingEntity = ThreadLocal.withInitial(() -> null);

	@Inject(method = "breakEgg", at = @At("HEAD"))
	private void onEggBrokenTurtleEggLogger(World world, BlockPos pos, BlockState state, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			TurtleEggLogger.getInstance().onBreakingEgg(world, pos, state, this.eggBreakingEntity.get());
		}
	}

	@Inject(method = "tryBreakEgg", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(World world, BlockState blockState, BlockPos blockPos, Entity entity, int i, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(entity);
		}
	}

	@Inject(method = "afterBreak", at = @At("HEAD"))
	private void recordEntityTurtleEggLogger(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack, CallbackInfo ci)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			this.eggBreakingEntity.set(player);
		}
	}
}
