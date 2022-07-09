package carpettisaddition.mixins.command.lifetime.deathdamage;

import carpettisaddition.commands.lifetime.interfaces.DamageableEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements DamageableEntity
{
	private DamageSource deathDamageSource;

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	private void onDeathLifeTimeTracker(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.deathDamageSource = source;
	}

	@Override
	public DamageSource getDeathDamageSource()
	{
		return this.deathDamageSource;
	}
}
