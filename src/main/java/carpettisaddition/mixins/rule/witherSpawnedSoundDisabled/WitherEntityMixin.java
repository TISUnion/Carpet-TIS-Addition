package carpettisaddition.mixins.rule.witherSpawnedSoundDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.boss.WitherEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(WitherEntity.class)
public abstract class WitherEntityMixin
{
	@ModifyArg(
			method = "mobTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;syncGlobalEvent(ILnet/minecraft/util/math/BlockPos;I)V"
			),
			index = 0
	)
	private int witherSpawnedSoundDisabled(int eventId)
	{
		if (CarpetTISAdditionSettings.witherSpawnedSoundDisabled)
		{
			eventId = -1;
		}
		return eventId;
	}
}
