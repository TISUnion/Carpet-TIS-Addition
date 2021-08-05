package carpettisaddition.mixins.command.info;

import carpet.utils.BlockInfo;
import carpettisaddition.commands.info.InfoCommand;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockInfo.class)
public abstract class BlockInfoMixin
{
	@Inject(method = "blockInfo", at = @At("TAIL"), remap = false)
	private static void showMoreBlockInfo(BlockPos pos, World world, CallbackInfoReturnable<List<BaseText>> cir)
	{
		cir.getReturnValue().addAll(InfoCommand.getInstance().showMoreBlockInfo(pos, world));
	}
}
