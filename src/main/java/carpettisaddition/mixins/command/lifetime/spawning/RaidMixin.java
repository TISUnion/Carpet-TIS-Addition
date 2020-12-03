package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public abstract class RaidMixin
{
	@Inject(
			method = "addRaider",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private void onEntitySpawnAsRaiderLifeTimeTracker(int wave, RaiderEntity raider, BlockPos pos, boolean existing, CallbackInfo ci)
	{
		((IEntity)raider).recordSpawning(LiteralSpawningReason.RAID);
	}
}
