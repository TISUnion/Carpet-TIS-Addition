package carpettisaddition.mixins.command.lifetime.spawning.blockdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@ModifyArg(
			//#if MC >= 11700
			//$$ method = "dropStack(Lnet/minecraft/world/World;Ljava/util/function/Supplier;Lnet/minecraft/item/ItemStack;)V",
			//#else
			method = "dropStack",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private static Entity onBlockDropsItemLifeTimeTracker(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(LiteralSpawningReason.BLOCK_DROP);
		return itemEntity;
	}
}
