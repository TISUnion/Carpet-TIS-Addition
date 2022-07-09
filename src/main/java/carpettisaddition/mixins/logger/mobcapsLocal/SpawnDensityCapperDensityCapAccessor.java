package carpettisaddition.mixins.logger.mobcapsLocal;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import it.unimi.dsi.fastutil.objects.Object2IntMap;
//$$ import net.minecraft.entity.SpawnGroup;
//$$ import net.minecraft.world.SpawnDensityCapper;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ SpawnDensityCapper.DensityCap.class
		//#else
		DummyClass.class
		//#endif
)
public interface SpawnDensityCapperDensityCapAccessor
{
	//#if MC >= 11800
	//$$ @Invoker("<init>")
	//$$ static SpawnDensityCapper.DensityCap invokeConstructor()
	//$$ {
	//$$ 	throw new AssertionError();
	//$$ }
	//$$
	//$$ @Accessor
	//$$ Object2IntMap<SpawnGroup> getSpawnGroupsToDensity();
	//#endif
}