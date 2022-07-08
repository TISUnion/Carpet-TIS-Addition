package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.BoatItem;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(BoatItem.class)
public abstract class BoatItemMixin
{
	@ModifyVariable(
			method = "use",
			at = @At(
					value = "INVOKE_ASSIGN",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",
					//#else
					target = "Lnet/minecraft/world/World;getEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private List<Entity> entityPlacementIgnoreCollision_skipEntityCheck(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			entities.clear();
		}
		return entities;
	}

	@Redirect(
			method = "use",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/World;isSpaceEmpty(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z"
					//#else
					target = "Lnet/minecraft/world/World;doesNotCollide(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z"
					//#endif
			),
			require = 0
	)
	private boolean entityPlacementIgnoreCollision_skipCollisionCheck(World world, Entity entity, Box box)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			return true;
		}
		// vanilla
		return world.doesNotCollide(entity, box);
	}
}
