package carpettisaddition.mixins.command.lifetime.spawning.mobthrow;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends Entity
{
	public FoxEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@ModifyVariable(
			method = {
					"spit",
					"dropItem"
			},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private ItemEntity onFoxThrowItemEntityLifeTimeTracker(ItemEntity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobThrowSpawningReason(this.getType()));
		return itemEntity;
	}
}
