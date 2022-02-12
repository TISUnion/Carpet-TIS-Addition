package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	@Redirect(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;isSpaceEmpty(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Z"
			),
			require = 0
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition1true(World world, Entity entity, Box box, Predicate<Entity> predicate)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			return true;
		}
		// vanilla
		return world.isSpaceEmpty(entity, box, predicate);
	}

	@Redirect(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;isEmpty()Z",
					remap = false
			),
			require = 0
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition2true(List<Entity> entityList)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			return true;
		}
		// vanilla
		return entityList.isEmpty();
	}
}
