package carpettisaddition.mixins.logger.mobcapsLocal;

import carpettisaddition.utils.ModIds;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnDensityCapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
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