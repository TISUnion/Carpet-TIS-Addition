package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Queue;

//#if MC >= 11800
//$$ import net.minecraft.world.tick.ChunkTickScheduler;
//$$ import net.minecraft.world.tick.OrderedTick;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(
		//#if MC >= 11800
		//$$ ChunkTickScheduler.class
		//#else
		DummyClass.class
		//#endif
)
public interface ChunkTickSchedulerAccessor
{
	//#if MC >= 11800
	//$$ @Accessor
	//$$ Queue<OrderedTick<?>> getTickQueue();
	//#endif
}