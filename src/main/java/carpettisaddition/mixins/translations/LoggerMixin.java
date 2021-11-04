package carpettisaddition.mixins.translations;

import carpet.logging.Logger;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(Logger.class)
public abstract class LoggerMixin
{
	@Unique
	private static final ThreadLocal<ServerPlayerEntity> currentLoggingPlayer = ThreadLocal.withInitial(() -> null);

	@Inject(method = "sendPlayerMessage", at = @At("HEAD"), remap = false)
	private void applyTISCarpetTranslation_recordPlayer(ServerPlayerEntity player, BaseText[] messages, CallbackInfo ci)
	{
		currentLoggingPlayer.set(player);
	}

	@ModifyArg(
			method = "sendPlayerMessage",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/stream/Stream;forEach(Ljava/util/function/Consumer;)V"
			),
			remap = false
	)
	private Consumer<? super BaseText> applyTISCarpetTranslation(Consumer<? super BaseText> textSender)
	{
		return text -> {
			BaseText tiscmTranslated = TISAdditionTranslations.translate(text, currentLoggingPlayer.get());
			textSender.accept(tiscmTranslated);
		};
	}
}
