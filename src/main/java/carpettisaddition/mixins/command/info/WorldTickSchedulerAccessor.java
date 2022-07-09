package carpettisaddition.mixins.command.info;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import net.minecraft.world.tick.ChunkTickScheduler;
//$$ import net.minecraft.world.tick.WorldTickScheduler;
//$$ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ WorldTickScheduler.class
		//#else
		DummyClass.class
		//#endif
)
public interface WorldTickSchedulerAccessor<T>
{
	//#if MC >= 11800
	//$$ @Accessor
	//$$ Long2ObjectMap<ChunkTickScheduler<T>> getChunkTickSchedulers();
	//#endif
}