package carpettisaddition.helpers.rule.hopperNoItemCost;

import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;

/**
 * Lithium mod uses its own optimized way for hoppers to transfer their items out
 * So the regular vanilla injection does not work any more
 * Here's the helper class to solve that
 */
public class HopperNoItemCostHelper
{
	public static final ThreadLocal<LithiumStackList> currentHopperInvList = ThreadLocal.withInitial(() -> null);
}
