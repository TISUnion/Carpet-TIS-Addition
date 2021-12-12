package carpettisaddition.helpers.rule.optimizedFastEntityMovement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.CollisionView;

@FunctionalInterface
public interface CollisionBoxGetter
{
	Iterable<VoxelShape> get(CollisionView world, Entity entity, Box box);
}
