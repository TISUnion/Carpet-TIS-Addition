package carpettisaddition.mixins.translations;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

/**
 * The language field getter doesn't exist in all MC versions, so here comes this accessor class
 * in mc1.18+ ClientSettingsC2SPacket has become a record class, so it became useless
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.18"))
@Mixin(DummyClass.class)
public interface ClientSettingsC2SPacketAccessor
{
}