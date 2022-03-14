package carpettisaddition.mixins.command.lifetime.spawning.portalpigman;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin
{
	@ModifyVariable(
			method = "randomTick",
			at = @At(
					value = "STORE",
					target = "Lnet/minecraft/entity/EntityType;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
			)
	)
	private Entity onPigmanSpawnedLifeTimeTracker(Entity entity)
	{
		if (entity != null)
		{
			((LifetimeTrackerTarget) entity).recordSpawning(LiteralSpawningReason.PORTAL_PIGMAN);
		}
		return entity;
	}
}
