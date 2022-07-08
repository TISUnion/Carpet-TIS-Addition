package carpettisaddition.mixins.command.info.entity;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(Entity.class)
public interface EntityAccessor
{
	//#if MC >= 11600
	//$$ @Accessor
	//$$ int getNetherPortalCooldown();
	//#endif
}