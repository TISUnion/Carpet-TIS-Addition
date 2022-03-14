package carpettisaddition.mixins.command.lifetime.spawning.blockdrop;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin
{
	@ModifyArg(
			method = "onBreak",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private Entity onBlockDropsItemLifeTimeTracker(Entity itemEntity)
	{
		((LifetimeTrackerTarget)itemEntity).recordSpawning(LiteralSpawningReason.BLOCK_DROP);
		return itemEntity;
	}
}
