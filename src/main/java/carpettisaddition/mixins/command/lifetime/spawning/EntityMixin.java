package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
import carpettisaddition.commands.lifetime.spawning.TransDimensionSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public World world;

	@Shadow public abstract EntityType<?> getType();

	@Inject(method = "changeDimension", at = @At("RETURN"))
	private void onEntityTransDimensionSpawnedLifeTimeTracker(CallbackInfoReturnable<Entity> cir)
	{
		Entity entity = cir.getReturnValue();
		if (entity != null)
		{
			((IEntity)entity).recordSpawning(new TransDimensionSpawningReason(this.world.getDimension().getType()));
		}
	}

	@Inject(method = "dropStack(Lnet/minecraft/item/ItemStack;F)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
	private void onItemEntitySpawnedByDroppingLifeTimeTracker(CallbackInfoReturnable<ItemEntity> cir)
	{
		ItemEntity itemEntity = cir.getReturnValue();
		if (itemEntity != null)
		{
			((IEntity)itemEntity).recordSpawning(new MobDropSpawningReason(this.getType()));
		}
	}
}
