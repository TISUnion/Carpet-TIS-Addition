package carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.*;

//#if MC >= 11600
//$$ import com.google.common.collect.Lists;
//$$ import org.spongepowered.asm.mixin.injection.Redirect;
//#else
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
//#endif

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
	private final Random random$TISCM = new Random();

	//#if MC >= 11600
	//$$ @Redirect(
	//$$ 		method = "update",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
	//$$ 		)
	//$$ )
	//$$ private Iterator<BlockPos> letsMakeTheOrderUnpredictable(Set<BlockPos> set)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
	//$$ 	{
	//$$ 		List<BlockPos> list = Lists.newArrayList(set);
	//$$ 		Collections.shuffle(list, this.random$TISCM);
	//$$ 		return list.iterator();
	//$$ 	}
	//$$ 	return set.iterator();
	//$$ }
	//#else
	@Inject(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;clear()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void letsMakeTheOrderUnpredictable(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir, List<BlockPos> list)
	{
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			Collections.shuffle(list, random$TISCM);
		}
	}
	//#endif
}
