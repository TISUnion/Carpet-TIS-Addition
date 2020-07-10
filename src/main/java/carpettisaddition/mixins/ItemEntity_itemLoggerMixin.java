package carpettisaddition.mixins;

import carpettisaddition.logging.logHelpers.itemLoggerHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemEntity.class)
public abstract class ItemEntity_itemLoggerMixin
{
	private boolean flagDied = false;
	private boolean flagDespawned = false;

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
	void onDespawned(CallbackInfo ci)
	{
		if (!this.flagDespawned)
		{
			itemLoggerHelper.onItemDespawn((ItemEntity) (Object) this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	void onDied(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (!this.flagDied)
		{
			itemLoggerHelper.onItemDie((ItemEntity)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
