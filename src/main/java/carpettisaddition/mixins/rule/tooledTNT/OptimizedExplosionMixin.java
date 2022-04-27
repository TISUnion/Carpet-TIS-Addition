package carpettisaddition.mixins.rule.tooledTNT;

import carpet.helpers.OptimizedExplosion;
import carpettisaddition.helpers.rule.tooledTNT.TooledTNTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OptimizedExplosion.class)
public abstract class OptimizedExplosionMixin
{
	/**
	 * See {@link ExplosionMixin} for mixin at vanilla in for this rule
	 */
	@Redirect(
			method = "doExplosionB",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/item/ItemStack;EMPTY:Lnet/minecraft/item/ItemStack;"
			),
			allow = 2
	)
	private static ItemStack useTheToolInYourHand(Explosion e, boolean spawnParticles)
	{
		return TooledTNTHelper.getMainHandItemOfCausingEntity(e);
	}
}
