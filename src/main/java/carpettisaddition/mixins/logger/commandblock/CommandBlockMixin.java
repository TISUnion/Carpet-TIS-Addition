package carpettisaddition.mixins.logger.commandblock;

import carpettisaddition.logging.loggers.commandblock.CommandBlockLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin
{
	@Inject(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/CommandBlockExecutor;execute(Lnet/minecraft/world/World;)Z",
					shift = At.Shift.AFTER
			)
	)
	private void onCommandBlockExecutedCommandBlockLogger(BlockState state, World world, BlockPos pos, CommandBlockExecutor executor, boolean hasCommand, CallbackInfo ci)
	{
		CommandBlockLogger.getInstance().onCommandBlockActivated(world, pos, state, executor);
	}
}
