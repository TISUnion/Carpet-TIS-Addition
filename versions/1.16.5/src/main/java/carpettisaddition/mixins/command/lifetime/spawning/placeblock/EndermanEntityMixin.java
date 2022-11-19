package carpettisaddition.mixins.command.lifetime.spawning.placeblock;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EndermanEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin
{
	@Inject(method = "setCarriedBlock", at = @At("TAIL"))
	private void itCountsTowardsMobcapAgainLifetimeTracker(BlockState state, CallbackInfo ci)
	{
		if (state == null)
		{
			((LifetimeTrackerTarget)this).recordSpawning(LiteralSpawningReason.PLACE_BLOCK);
		}
	}
}