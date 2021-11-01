package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobThrowSpawningReason;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, World world)
	{
		super(type, world);
	}

	@Inject(method = "dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;", at = @At("RETURN"))
	private void onItemEntitySpawnedByDroppingLifeTimeTracker(CallbackInfoReturnable<ItemEntity> cir)
	{
		ItemEntity itemEntity = cir.getReturnValue();
		if (itemEntity != null)
		{
			((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobThrowSpawningReason(this.getType()));
		}
	}
}
