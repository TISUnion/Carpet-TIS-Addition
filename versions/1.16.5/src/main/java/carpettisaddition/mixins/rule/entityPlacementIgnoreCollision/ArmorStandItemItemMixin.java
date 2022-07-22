package carpettisaddition.mixins.rule.entityPlacementIgnoreCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import net.minecraft.util.math.Box;
import java.util.function.Predicate;

/**
 * 1.15- mixin is handled in the version-specified class
 */
@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	@Redirect(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/World;isSpaceEmpty(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z"
					//#else
					target = "Lnet/minecraft/world/World;isSpaceEmpty(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Z"
					//#endif
			),
			require = 0
	)
	private boolean entityPlacementIgnoreCollision_makeIfCondition1true(
			World world, Entity entity, Box box
			//#if MC < 11800
			, Predicate<Entity> predicate
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.entityPlacementIgnoreCollision)
		{
			return true;
		}
		// vanilla
		return world.isSpaceEmpty(
	 			entity, box
				//#if MC < 11800
				, predicate
				//#endif
		);
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
