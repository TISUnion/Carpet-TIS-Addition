package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin extends Entity
{
	public AllayEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "loot",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/passive/AllayEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"
			)
	)
	private void onDolphinPickUpItemLifeTimeTracker(ItemEntity item, CallbackInfo ci)
	{
		((LifetimeTrackerTarget)item).recordRemoval(new MobPickupRemovalReason(this.getType()));
	}
}
