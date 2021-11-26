package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyConstant(method = "attemptTickInVoid", constant = @Constant(intValue = 64), allow = 1, require = 0)
	private int modifyVoidRelatedAltitude(int value)
	{
		return (int)MathHelper.clamp(Math.round(-CarpetTISAdditionSettings.voidRelatedAltitude), 0, Integer.MAX_VALUE);
	}
}
