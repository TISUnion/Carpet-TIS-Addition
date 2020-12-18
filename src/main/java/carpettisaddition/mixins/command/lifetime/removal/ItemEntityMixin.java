package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.IEntity;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity
{
	public ItemEntityMixin(EntityType<?> type, World world)
	{
		super(type, world);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(LiteralRemovalReason.DESPAWN_TIMEOUT);
	}

	@Inject(
			method = "merge(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	private static void onMergedLifeTimeTracker(ItemEntity targetEntity, ItemStack targetStack, ItemEntity sourceEntity, ItemStack sourceStack, CallbackInfo ci)
	{
		((IEntity)sourceEntity).recordRemoval(LiteralRemovalReason.MERGE);
	}

	@Inject(
			method = "onPlayerCollision",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
			)
	)
	private void onPickupLifeTimeTracker(PlayerEntity player, CallbackInfo ci)
	{
		((IEntity)this).recordRemoval(new MobPickupRemovalReason(player.getType()));
	}
}
