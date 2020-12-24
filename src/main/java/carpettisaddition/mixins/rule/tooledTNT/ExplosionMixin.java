package carpettisaddition.mixins.rule.tooledTNT;

import carpettisaddition.helpers.TooledTNTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Explosion.class)
public abstract class ExplosionMixin
{
	/**
	 * See {@link OptimizedExplosionMixin} for mixin at carpet in for this rule
	 */
	@Redirect(
			method = "affectWorld",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/item/ItemStack;EMPTY:Lnet/minecraft/item/ItemStack;"
			),
			allow = 1
	)
	private ItemStack useTheToolInYourHand()
	{
		return TooledTNTHelper.getMainHandItemOfCausingEntity((Explosion)(Object)this);
	}
}
