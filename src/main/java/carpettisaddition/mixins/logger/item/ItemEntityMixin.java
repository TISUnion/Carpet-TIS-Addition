package carpettisaddition.mixins.logger.item;

import carpettisaddition.logging.loggers.entity.ItemLogger;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	private boolean flagDied = false;
	private boolean flagDespawned = false;

	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void onCreated(CallbackInfo ci)
	{
		ItemLogger.getInstance().onEntityCreated((ItemEntity)(Object)this);
	}

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
					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
			)
	)
	void onDespawned(CallbackInfo ci)
	{
		if (!this.flagDespawned)
		{
			ItemLogger.getInstance().onEntityDespawn((ItemEntity)(Object)this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;discard()V"
			)
	)
	void onDied(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (!this.flagDied)
		{
			ItemLogger.getInstance().onEntityDied((ItemEntity)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
