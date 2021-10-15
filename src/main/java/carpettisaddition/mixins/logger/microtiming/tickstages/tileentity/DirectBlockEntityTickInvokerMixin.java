package carpettisaddition.mixins.logger.microtiming.tickstages.tileentity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.interfaces.IWorldTileEntity;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileEntitySubStage;
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

	@Inject(method = "tick()V", at = @At("HEAD"))
	private void startTileEntitySection(CallbackInfo ci)
	{
		BlockEntity blockEntity = this.blockEntity;
		World world = blockEntity.getWorld();
		if (world != null)
		{
			int counter = ((IWorldTileEntity)world).getTileEntityOrderCounter();
			((IWorldTileEntity) world).setTileEntityOrderCounter(counter + 1);
			MicroTimingLoggerManager.setSubTickStage(world, new TileEntitySubStage(blockEntity, counter));
		}
	}
}
