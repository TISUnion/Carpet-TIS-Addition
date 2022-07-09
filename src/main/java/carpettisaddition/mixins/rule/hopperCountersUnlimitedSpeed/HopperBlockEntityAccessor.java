package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Used in mc 1.17+
 */
@Mixin(HopperBlockEntity.class)
public interface HopperBlockEntityAccessor
{
	//#if MC >= 11700
	//$$ @Invoker
	//$$ boolean callIsFull();
	//$$
	//$$ @Invoker
	//$$ void callSetCooldown(int cooldown);
	//#endif
}