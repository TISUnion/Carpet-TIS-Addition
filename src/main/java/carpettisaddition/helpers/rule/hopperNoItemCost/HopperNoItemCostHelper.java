package carpettisaddition.helpers.rule.hopperNoItemCost;

import net.minecraft.util.DyeColor;

/**
 * Used in mc1.17+
 *
 * Lithium mod uses its own optimized way for hoppers to transfer their items out
 * So the regular vanilla injection might not work
 * Here's the helper class to solve that
 */
public class HopperNoItemCostHelper
{
	/**
	 * null -> disabled
	 * other -> enabled with wool color
	 */
	public static final ThreadLocal<DyeColor> woolColor = ThreadLocal.withInitial(() -> null);
}