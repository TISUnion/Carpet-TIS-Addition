package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.GameUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EndCrystalItem.class)
public abstract class EndCrystalItemMixin
{
	@ModifyArg(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;isAir(Lnet/minecraft/util/math/BlockPos;)Z"
			)
	)
	private BlockPos entityPlacementIgnoreCollision_skipAirCheck(BlockPos pos)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			pos = GameUtil.getInvalidBlockPos();
		}
		return pos;
	}

	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;",
					shift = At.Shift.AFTER
			)
	)
	private List<Entity> entityPlacementIgnoreCollision_skipEntityCheck(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			entities.clear();
		}
		return entities;
	}
}
