package carpettisaddition.helpers.rule.optimizedFastEntityMovement;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class OFEMContext
{
	// basic info
	public final World world;
	public final Entity entity;

	// for per getAxisOnlyBlockCollision call
	public Direction.Axis axis;
	public double movementOnAxis;
	public Box entityBoundingBox;

	public OFEMContext(World world, Entity entity)
	{
		this.world = world;
		this.entity = entity;
	}
}
