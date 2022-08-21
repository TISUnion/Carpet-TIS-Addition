package carpettisaddition.mixins.rule.largeBarrel.compat.fabricapi;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = @Condition(type = Condition.Type.MIXIN, value = "carpettisaddition.mixins.rule.largeBarrel.compat.fabricapi.ItemStorageMixin"))
@Mixin(DoubleInventory.class)
public interface DoubleInventoryAccessor
{
	@Accessor
	Inventory getFirst();

	@Accessor
	Inventory getSecond();
}
