package carpettisaddition.mixins.command.manipulate;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11700
//$$ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
//$$ import net.minecraft.entity.Entity;
//$$ import net.minecraft.world.EntityList;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif


@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(
		//#if MC >= 11700
		//$$ EntityList.class
		//#else
		DummyClass.class
		//#endif
)
public interface EntityListAccessor
{
	//#if MC >= 11700
	//$$ @Invoker
	//$$ void invokeEnsureSafe();
	//$$
	//$$ @Accessor
	//$$ Int2ObjectMap<Entity> getEntities();
	//#endif
}