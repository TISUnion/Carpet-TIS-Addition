package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(HopperBlockEntity.class)
public interface HopperBlockEntityAccessor
{
	@Invoker
	boolean invokeIsFull();

	@Invoker
	void invokeSetCooldown(int cooldown);
}