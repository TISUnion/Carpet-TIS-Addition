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

	@Inject(
			method = "onGameMessage",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;messageCooldown:I",
					ordinal = 0
			)
	)
	private void resetMessageCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.messageCooldown = 0;
		}
	}

	@Inject(
			method = "onCreativeInventoryAction",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;creativeItemDropThreshold:I",
					ordinal = 0
			)
	)
	private void resetCreativeItemDropThreshold(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.creativeItemDropThreshold = 0;
		}
	}
}
