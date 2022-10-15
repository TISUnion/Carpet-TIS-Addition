package carpettisaddition.mixins.rule.flattenTriangularDistribution;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(Random.class)
public interface RandomMixin
{
	@Shadow double nextDouble();

	@Inject(method = "nextTriangular", at = @At("HEAD"), cancellable = true)
	default void nextTriangular(double mode, double deviation, CallbackInfoReturnable<Double> cir)
	{
		if (CarpetTISAdditionSettings.flattenTriangularDistribution)
		{
			this.nextDouble();
			cir.setReturnValue(mode + deviation * (-1 + this.nextDouble() * 2));
		}
	}
}
