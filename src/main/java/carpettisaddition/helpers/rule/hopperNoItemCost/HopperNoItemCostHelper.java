package carpettisaddition.helpers.rule.hopperNoItemCost;

import net.minecraft.block.entity.HopperBlockEntity;

/**
 * Lithium mod uses its own optimized way for hoppers to transfer their items out
 * So the regular vanilla injection does not work any more
 * Here's the helper class to solve that
 */
public class HopperNoItemCostHelper
{
	public static final ThreadLocal<HopperBlockEntity> currentHopper = ThreadLocal.withInitial(() -> null);
}
