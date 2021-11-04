package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EntityView;
import net.minecraft.world.RegistryWorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

// It's almost the same as interface IWorld in < 1.16
@Mixin(value = RegistryWorldView.class, priority = 2000)
public interface RegistryWorldViewMixin extends EntityView
{
	/**
	 * @reason Interface injection is not supported by Mixin yet
	 * So here comes the @Overwrite
	 *
	 * @author Fallen_Breath
	 */
	@Overwrite
	default List<VoxelShape> getEntityCollisions(@Nullable Entity entity, Box box)
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

			// vanilla copy
			return EntityView.super.getEntityCollisions(entity, box);
		}
		finally
		{
			OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.set(false);
		}
	}
}
