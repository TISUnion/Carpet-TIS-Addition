package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(require = @Condition(ModIds.malilib))
@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin
{
	@ModifyVariable(
			method = "get",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/BlockView;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;"
			)
	)
	private BlockEntity letsHaveAnOffThreadAccess(BlockEntity blockEntity, BlockView blockView, BlockPos pos)
	{
		if (CarpetTISAdditionSettings.largeBarrel && LargeBarrelHelper.enabledOffThreadBlockEntityAccess.get())
		{
			if (blockView instanceof World)
			{
				World world = (World)blockView;
				blockEntity = world.getChunk(pos).getBlockEntity(pos);
			}
		}
		return blockEntity;
	}
}
