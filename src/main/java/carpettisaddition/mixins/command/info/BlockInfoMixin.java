package carpettisaddition.mixins.command.info;

import carpet.utils.BlockInfo;
import carpettisaddition.commands.info.InfoCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockInfo.class)
public abstract class BlockInfoMixin
{
	@Inject(method = "blockInfo", at = @At("TAIL"), remap = false)
	private static void showMoreBlockInfo(BlockPos pos, ServerWorld world, CallbackInfoReturnable<List<MutableText>> cir)
	{
		cir.getReturnValue().addAll(InfoCommand.getInstance().showMoreBlockInfo(pos, world));
	}
}
