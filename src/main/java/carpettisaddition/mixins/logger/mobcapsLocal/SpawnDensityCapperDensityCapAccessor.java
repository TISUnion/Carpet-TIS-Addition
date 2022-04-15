package carpettisaddition.mixins.logger.mobcapsLocal;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnDensityCapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnDensityCapper.DensityCap.class)
public interface SpawnDensityCapperDensityCapAccessor
{
	@Invoker("<init>")
	static SpawnDensityCapper.DensityCap invokeConstructor()
	{
		throw new AssertionError();
	}

	@Accessor
	Object2IntMap<SpawnGroup> getSpawnGroupsToDensity();
}
