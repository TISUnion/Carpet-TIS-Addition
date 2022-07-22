package carpettisaddition.mixins.command.info;

import carpettisaddition.utils.ModIds;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(WorldTickScheduler.class)
public interface WorldTickSchedulerAccessor<T>
{
	@Accessor
	Long2ObjectMap<ChunkTickScheduler<T>> getChunkTickSchedulers();
}