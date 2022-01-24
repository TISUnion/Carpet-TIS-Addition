package carpettisaddition.mixins.rule.chatLengthLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.chatLengthLimit.ChatScreenTextFieldHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin
{
	@Shadow protected TextFieldWidget chatField;

	@ModifyArg(
			method = "init",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setMaxLength(I)V"
			)
	)
	private int modifyChatLengthLimitAtRead(int maxLength)
	{
		ChatScreenTextFieldHandler.CHAT_SCREEN_TEXT_FIELD_WIDGETS.add(this.chatField);
		return CarpetTISAdditionSettings.chatLengthLimit;
	}
}
