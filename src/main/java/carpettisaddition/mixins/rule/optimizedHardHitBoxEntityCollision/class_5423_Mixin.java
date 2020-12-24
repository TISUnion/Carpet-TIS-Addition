package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import net.minecraft.class_5423;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EntityView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(value = class_5423.class, priority = 2000)
public interface class_5423_Mixin extends EntityView
{
	/**
	 * @reason Interface injection is not supported by Mixin yet
	 * So here comes the @Overwrite
	 *
	 * @author Fallen_Breath
	 */
	@Overwrite
	default Stream<VoxelShape> getEntityCollisions(Entity entity, Box box, Predicate<Entity> predicate)
	{
		try
		{
			if (CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision)
			{
				if (!OptimizedHardHitBoxEntityCollisionHelper.treatsGeneralEntityAsHardHitBox(entity))
				{
					OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.set(true);
				}
			}

			// vanill copy
			return EntityView.super.getEntityCollisions(entity, box, predicate);
		}
		finally
		{
			OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.set(false);
		}
	}
}
