package carpettisaddition.mixins.rule.creativeNoItemCooldown;

import carpettisaddition.helpers.rule.creativeNoItemCooldown.ItemCooldownManagerWithPlayer;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{
	@Shadow @Final private ItemCooldownManager itemCooldownManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void creativeNoItemCooldown_attachPlayer(CallbackInfo ci)
	{
		((ItemCooldownManagerWithPlayer)this.itemCooldownManager).setPlayer$TISCM((PlayerEntity)(Object)this);
	}
}
