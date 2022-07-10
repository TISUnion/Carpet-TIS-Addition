package carpettisaddition.mixins.rule.enchantCommandNoRestriction;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.EnchantCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Collections;

@Mixin(value = EnchantCommand.class, priority = 900)
public abstract class EnchantCommandMixin
{
	@Redirect(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/Enchantment;getMaximumLevel()I"
			),
			require = 0  // for potential mod compatibility
	)
	private static int removeLevelRestriction(Enchantment enchantment)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			return Integer.MAX_VALUE;
		}
		// vanilla copy
		return enchantment.getMaximumLevel();
	}

	@Redirect(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"
			),
			require = 0  // for potential mod compatibility
	)
	private static boolean removeAcceptableCheck(Enchantment enchantment, ItemStack stack)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			return true;
		}
		// vanilla copy
		return enchantment.isAcceptableItem(stack);
	}

	@ModifyArg(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/EnchantmentHelper;contains(Ljava/util/Collection;Lnet/minecraft/enchantment/Enchantment;)Z"
			),
			index = 0
	)
	private static Collection<Enchantment> removeExistingCheck(Collection<Enchantment> enchantments)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			return Collections.emptySet();
		}
		return enchantments;
	}
}
