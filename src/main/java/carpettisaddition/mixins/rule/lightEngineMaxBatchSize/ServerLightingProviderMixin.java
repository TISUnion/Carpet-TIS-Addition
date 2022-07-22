package carpettisaddition.mixins.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin
{
	@Shadow public abstract void setTaskBatchSize(int taskBatchSize);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void adjustBatchSize(CallbackInfo ci)
	{
		this.setTaskBatchSize(CarpetTISAdditionSettings.lightEngineMaxBatchSize);
	}
}
