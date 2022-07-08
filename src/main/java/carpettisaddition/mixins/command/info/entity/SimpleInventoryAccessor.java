package carpettisaddition.mixins.command.info.entity;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(BasicInventory.class)
public interface SimpleInventoryAccessor
{
	//#if MC >= 11600
	//$$ @Accessor
	//$$ DefaultedList<ItemStack> getStacks();
	//#endif
}