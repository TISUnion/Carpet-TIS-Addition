package carpettisaddition.mixins.rule.antiSpamDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Shadow private int messageCooldown;

	@Shadow private int creativeItemDropThreshold;

	@Inject(method = "onChatMessage", at = @At(value = "TAIL"))
	private void resetMessageCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.messageCooldown = 0;
		}
	}

	@Inject(method = "onCreativeInventoryAction", at = @At("TAIL"))
	private void resetCreativeItemDropThreshold(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.creativeItemDropThreshold = 0;
		}
	}
}
