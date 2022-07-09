package carpettisaddition.mixins.command.lifetime.spawning.mobdrop;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
//$$ import carpettisaddition.commands.lifetime.spawning.MobDropSpawningReason;
//$$ import net.minecraft.entity.Entity;
//$$ import net.minecraft.entity.EntityType;
//$$ import net.minecraft.item.ItemUsage;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(
		//#if MC >= 11700
		//$$ ItemUsage.class
		//#else
		DummyClass.class
		//#endif
)
public abstract class ItemUsageMixin
{
	//#if MC >= 11700
	//$$ /**
	//$$  * Currently only used in onItemEntityDestroyed for bundle item and shulker box item
	//$$  */
	//$$ @ModifyArg(
	//$$ 		method = "method_33265",  // lambda method in spawnItemContents
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
	//$$ 				remap = true
	//$$ 		),
	//$$ 		remap = false
	//$$ )
	//$$ private static Entity recordItemEntitySpawnedByItemEntityDestroyedLifeTimeTracker(Entity itemEntity)
	//$$ {
	//$$ 	((LifetimeTrackerTarget)itemEntity).recordSpawning(new MobDropSpawningReason(EntityType.ITEM));
	//$$ 	return itemEntity;
	//$$ }
	//#endif
}