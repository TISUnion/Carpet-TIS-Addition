package carpettisaddition.mixins.translations;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import org.spongepowered.asm.mixin.Mixin;

//#if MC < 11800
import org.spongepowered.asm.mixin.gen.Accessor;
//#endif

/**
 * The language field getter doesn't exist in all MC versions, so here comes this accessor class
 * in mc1.18+ ClientSettingsC2SPacket has become a record class, so it became useless
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.18"))
@Mixin(ClientSettingsC2SPacket.class)
public interface ClientSettingsC2SPacketAccessor
{
	//#if MC < 11800
	@Accessor("language")
	String getLanguage$TISCM();
	//#endif
}