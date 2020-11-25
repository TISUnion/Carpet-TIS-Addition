package carpettisaddition.mixins.carpet.tntRandomRange;

import carpettisaddition.helpers.tntRandomRange.IWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(World.class)
public class WorldMixin implements IWorld
{
	@Mutable
	@Shadow @Final public Random random;

	@Override
	public void setRandomCTA(Random random)
	{
		this.random = random;
	}
}
