package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11600
//$$ import net.minecraft.util.ActionResult;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#else
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
//#endif

@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	//#if MC >= 11600
	//$$
	//$$ @Inject(
	//$$ 		method = "useOnBlock",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				//#if MC >= 11700
	//$$ 				//$$ target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
	//$$ 				//#else
	//$$ 				target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
	//$$ 				//#endif
	//$$ 		)
	//$$ )
	//$$ private void preciseEntityPlacement(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.preciseEntityPlacement)
	//$$ 	{
	//$$ 		PreciseEntityPlacer.spawnEggTargetPos.set(context.getHitPos());
	//$$ 	}
	//$$ }
	//#else
	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;refreshPositionAndAngles(DDDFF)V",
					shift = At.Shift.AFTER
			)
	)
	private ArmorStandEntity preciseEntityPlacement(ArmorStandEntity armorStandEntity, ItemUsageContext context)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntity(armorStandEntity, context);
		}
		return armorStandEntity;
	}
	//#endif
}
