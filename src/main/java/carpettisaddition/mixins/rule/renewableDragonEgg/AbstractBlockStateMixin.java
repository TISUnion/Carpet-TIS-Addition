package carpettisaddition.mixins.rule.renewableDragonEgg;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Lithium block.flatten_states uses hasRandomTicks cache, disable it bruteforcely
 * Mixin priority = 1500, higher than lithium
 */
@Mixin(value = AbstractBlock.AbstractBlockState.class, priority = 1500)
public abstract class AbstractBlockStateMixin
{
	@Shadow public abstract Block getBlock();

	@Shadow protected abstract BlockState asBlockState();

	/**
	 * @author Fallen_Breath
	 * @reason Force using vanilla method to make sure the result is correct
	 * in case lithium mod overwrites it and uses its immutable cache
	 */
	@Overwrite
	public boolean hasRandomTicks()
	{
		// vanilla copy
		return this.getBlock().hasRandomTicks(this.asBlockState());
	}
}
