package carpettisaddition.mixins.rule.tileTickLimit;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Constant;
//$$ import org.spongepowered.asm.mixin.injection.ModifyConstant;
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	//#if MC >= 11800
	//$$ @ModifyConstant(
	//$$ 		method = "tick",
	//$$ 		slice = @Slice(
	//$$ 				from = @At(
	//$$ 						value = "CONSTANT",
	//$$ 						args = "stringValue=tickPending"
	//$$ 				),
	//$$ 				to = @At(
	//$$ 						value = "CONSTANT",
	//$$ 						args = "stringValue=raid"
	//$$ 				)
	//$$ 		),
	//$$ 		constant = @Constant(intValue = 65536),
	//$$ 		require = 2
	//$$ )
	//$$ private static int modifyTileTickLimit(int value)
	//$$ {
	//$$ 	return CarpetTISAdditionSettings.tileTickLimit;
	//$$ }
	//#endif
}