package carpettisaddition.helpers.rule.hopperNoItemCost;

/**
 * Lithium mod uses its own optimized way for hoppers to transfer their items out
 * So the regular vanilla injection might not work
 * Here's the helper class to solve that
 */
public class HopperNoItemCostHelper
{
	public static final ThreadLocal<Boolean> enable = ThreadLocal.withInitial(() -> false);
}
