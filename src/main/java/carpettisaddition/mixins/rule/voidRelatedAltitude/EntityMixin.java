package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//#if MC >= 11700
//$$ import net.minecraft.util.math.MathHelper;
//#endif

@Mixin(Entity.class)
public abstract class EntityMixin
{
	//#if MC >= 11700
	//$$
	//$$ @ModifyConstant(method = "attemptTickInVoid", constant = @Constant(intValue = 64), allow = 1, require = 0)
	//$$ private int modifyVoidRelatedAltitude(int value)
	//$$ {
	//$$ 	return (int)MathHelper.clamp(Math.round(-CarpetTISAdditionSettings.voidRelatedAltitude), 0, Integer.MAX_VALUE);
	//$$ }
	//#else
	@ModifyConstant(method = "baseTick", constant = @Constant(doubleValue = -64.0D), allow = 1, require = 0)
	private double modifyVoidRelatedAltitude(double value)
	{
		return CarpetTISAdditionSettings.voidRelatedAltitude;
	}
	//#endif
}
