package carpettisaddition.mixins.carpet.events.onServerLoadedWorlds;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin
{
	//#if MC < 11600
	@Inject(method = "loadWorld", at = @At("TAIL"))
	private void onSetupServerIntegrated(CallbackInfo ci)
	{
		CarpetTISAdditionServer.INSTANCE.onServerLoadedWorlds$TISCM((IntegratedServer) (Object) this);
	}
	//#endif
}
