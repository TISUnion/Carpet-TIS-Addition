package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.compact.CarpetSettings;
import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/Entity;D)Lnet/minecraft/entity/player/PlayerEntity;"
			)
	)
	private void creativeNoClipEnhancementEnter(CallbackInfo ci)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(true);
		}
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/Entity;D)Lnet/minecraft/entity/player/PlayerEntity;",
					shift = At.Shift.AFTER
			)
	)
	private void creativeNoClipEnhancementExit(CallbackInfo ci)
	{
		if (CarpetSettings.creativeNoClip)
		{
			CreativeNoClipHelper.ignoreNoClipPlayersFlag.set(false);
		}
	}
}
