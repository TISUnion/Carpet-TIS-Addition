package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.interfaces.IWorld_microTimingLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickstages.TileEntityTickStageExtra;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$DirectBlockEntityTickInvoker")
public abstract class DirectBlockEntityTickInvokerMixin<T extends BlockEntity>
{
	@Shadow @Final private T blockEntity;

	@Inject(method = "tick", at = @At("HEAD"))
	private void startTileEntitySection(CallbackInfo ci)
	{
		BlockEntity blockEntity = this.blockEntity;
		World world = blockEntity.getWorld();
		if (world != null)
		{
			int counter = ((IWorld_microTimingLogger) world).getTileEntityOrderCounter();
			((IWorld_microTimingLogger) world).setTileEntityOrderCounter(counter + 1);
			MicroTimingLoggerManager.setTickStageExtra(world, new TileEntityTickStageExtra(blockEntity, counter));
		}
	}
}
