package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

/**
 * 1.19+ manual stack update chain logic, for block /state update event stuffs
 * impl see 1.19 subproject
 */
public abstract class ChainRestrictedNeighborUpdaterMixins
{
	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(DummyClass.class)
	public static class SimpleEntryMixin
	{
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(DummyClass.class)
	public static class StatefulEntryMixin
	{
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(DummyClass.class)
	public static class SixWayEntryMixin
	{
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(DummyClass.class)
	public static class StateReplacementEntryMixin
	{
	}
}