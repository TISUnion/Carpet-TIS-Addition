package carpettisaddition.mixins;

import carpettisaddition.logging.logHelpers.itemLoggerHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemEntity.class)
public abstract class ItemEntity_itemLoggerMixin
{
	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	void onDespawn(CallbackInfo ci)
	{
		itemLoggerHelper.onItemDespawn((ItemEntity)(Object)this);
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	void onDie(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		itemLoggerHelper.onItemDie((ItemEntity)(Object)this, source, amount);
	}
}
