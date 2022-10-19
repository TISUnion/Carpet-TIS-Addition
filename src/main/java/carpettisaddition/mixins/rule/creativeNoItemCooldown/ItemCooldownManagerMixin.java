package carpettisaddition.mixins.rule.creativeNoItemCooldown;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.creativeNoItemCooldown.ItemCooldownManagerWithPlayer;
import carpettisaddition.utils.EntityUtil;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCooldownManager.class)
public abstract class ItemCooldownManagerMixin implements ItemCooldownManagerWithPlayer
{
	private PlayerEntity player$TISCM = null;

	@Override
	public void setPlayer$TISCM(PlayerEntity player$TISCM)
	{
		this.player$TISCM = player$TISCM;
	}

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private void creativeNoItemCooldown_preventSettingCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.creativeNoItemCooldown)
		{
			if (EntityUtil.isCreativePlayer(this.player$TISCM))
			{
				ci.cancel();
			}
		}
	}
}
