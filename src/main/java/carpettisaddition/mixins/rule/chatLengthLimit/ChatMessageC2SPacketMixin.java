package carpettisaddition.mixins.rule.chatLengthLimit;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ChatMessageC2SPacket.class)
public abstract class ChatMessageC2SPacketMixin
{
	// for the side
	@ModifyConstant(
			method = "<init>(Ljava/lang/String;)V",
			constant = @Constant(intValue = CarpetTISAdditionSettings.VANILLA_CHAT_LENGTH_LIMIT),
			require = 2
	)
	private int modifyChatLengthLimitAtConstruct(int lengthLimit)
	{
		return CarpetTISAdditionSettings.chatLengthLimit;
	}

	// server side
	@ModifyConstant(
			method = "read",
			constant = @Constant(intValue = CarpetTISAdditionSettings.VANILLA_CHAT_LENGTH_LIMIT),
			require = 1
	)
	private int modifyChatLengthLimitAtRead(int lengthLimit)
	{
		return CarpetTISAdditionSettings.chatLengthLimit;
	}
}
