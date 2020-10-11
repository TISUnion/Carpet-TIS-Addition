package carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;


@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block
{
	private final Random random = new Random();

	public RedstoneWireBlockMixin(Settings settings)
	{
		super(settings);
	}

	@Inject(
			method = "update",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private void letsMakeTheOrderUnpredictable(World world, BlockPos pos, BlockState state, CallbackInfo ci, Set<BlockPos> set)
	{
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			List<BlockPos> list = Lists.newArrayList(set);
			Collections.shuffle(list, random);
			for (BlockPos blockPos: list)
			{
				world.updateNeighborsAlways(blockPos, this);
			}
			ci.cancel();
		}
	}
}
