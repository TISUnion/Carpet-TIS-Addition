package carpettisaddition.mixins.translations;

import carpet.utils.HUDController;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	@Unique
	private static final ThreadLocal<PlayerEntity> currentLoggingPlayer = ThreadLocal.withInitial(() -> null);

	@Inject(method = "addMessage", at = @At("HEAD"), remap = false)
	private static void applyTISCarpetTranslation_recordPlayer(PlayerEntity player, BaseText hudMessage, CallbackInfo ci)
	{
		currentLoggingPlayer.set(player);
	}

	@ModifyArg(
			method = "addMessage",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
					ordinal = 1
			),
			remap = false
	)
	private static Object applyTISCarpetTranslation(Object text)
	{
		PlayerEntity player = currentLoggingPlayer.get();
		if (player instanceof ServerPlayerEntity && text instanceof BaseText)
		{
			return TISAdditionTranslations.translate((BaseText)text, (ServerPlayerEntity)player);
		};
		return text;
	}
}
