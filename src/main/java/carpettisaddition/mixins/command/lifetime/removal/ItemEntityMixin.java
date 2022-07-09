package carpettisaddition.mixins.command.lifetime.removal;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.LiteralRemovalReason;
import carpettisaddition.commands.lifetime.removal.MobPickupRemovalReason;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	private void onDespawnLifeTimeTracker(CallbackInfo ci)
	{
		((LifetimeTrackerTarget)this).recordRemoval(LiteralRemovalReason.DESPAWN_TIMEOUT);
	}

	@Inject(
			method = "merge(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;)V",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	private static void onMergedLifeTimeTracker(ItemEntity targetEntity, ItemStack targetStack, ItemEntity sourceEntity, ItemStack sourceStack, CallbackInfo ci)
	{
		// the recorded item stack count will be 0, it should be fine
		((LifetimeTrackerTarget)sourceEntity).recordRemoval(LiteralRemovalReason.MERGE);
	}

	@Inject(
			method = "onPlayerCollision",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onPickupLifeTimeTracker(PlayerEntity player, CallbackInfo ci, ItemStack itemStack, Item item, int i)
	{
		int stackCount = itemStack.getCount();
		itemStack.setCount(i);
		((LifetimeTrackerTarget)this).recordRemoval(new MobPickupRemovalReason(player.getType()));
		itemStack.setCount(stackCount);
	}
}
