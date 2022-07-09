package carpettisaddition.mixins.carpet.tweaks.rule.tntRandomRange;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11900
//$$ import net.minecraft.util.math.random.Random;
//#else
import java.util.Random;
//#endif

@Mixin(World.class)
public interface WorldAccessor
{
	@Mutable
	@Accessor
	//#disable-remap
	void setRandom(Random random);
	//#enable-remap
}
