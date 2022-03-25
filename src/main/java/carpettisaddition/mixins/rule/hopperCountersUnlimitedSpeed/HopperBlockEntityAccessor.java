package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HopperBlockEntity.class)
public interface HopperBlockEntityAccessor
{
	@Invoker
	boolean callIsFull();

	@Invoker("setTransferCooldown")
	void callSetCooldown(int cooldown);
}
