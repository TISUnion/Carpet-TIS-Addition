package carpettisaddition.mixins.translations;

import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientSettingsC2SPacket.class)
public interface ClientSettingsC2SPacketAccessor
{
	/**
	 * The language field getter doesn't exist in all MC versions, so here comes ours
	 */
	@Accessor("language")
	String getLanguage$TISCM();
}