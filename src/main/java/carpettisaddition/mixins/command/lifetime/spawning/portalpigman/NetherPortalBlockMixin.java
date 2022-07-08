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
			//#if MC >= 11600
			//$$ method = "randomTick",
			//#elseif MC >= 11500
			method = "scheduledTick",
			//#else
			//$$ method = "onScheduledTick",
			//#endif
			at = @At(
					value = "STORE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/entity/EntityType;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;"
					//#else
					target = "Lnet/minecraft/entity/EntityType;spawn(Lnet/minecraft/world/World;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/text/Text;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnType;ZZ)Lnet/minecraft/entity/Entity;"
					//#endif
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
