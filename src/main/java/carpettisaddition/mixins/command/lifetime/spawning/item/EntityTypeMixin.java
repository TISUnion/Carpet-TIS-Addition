package carpettisaddition.mixins.command.lifetime.spawning.item;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin
{
	@Inject(method = "spawnFromItemStack", at = @At("TAIL"))
	private void onEntitySpawnFromItemLifeTimeTracker(CallbackInfoReturnable<Entity> cir)
	{
		Entity entity = cir.getReturnValue();
		if (entity != null)
		{
			((LifetimeTrackerTarget)entity).recordSpawning(LiteralSpawningReason.ITEM);
		}
	}
}
