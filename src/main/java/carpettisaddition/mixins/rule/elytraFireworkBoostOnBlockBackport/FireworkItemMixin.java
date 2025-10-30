/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.rule.elytraFireworkBoostOnBlockBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.21.6"))
@Mixin(FireworkRocketItem.class)
public abstract class FireworkItemMixin
{
	@Inject(
			method = "useOn",
			at = @At("HEAD"),
			cancellable = true
	)
	private void elytraFireworkBoostOnBlockBackport_disallowIfThePlayerIsFallFlying(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir)
	{
		if (CarpetTISAdditionSettings.elytraFireworkBoostOnBlockBackport)
		{
			if (context.getPlayer() != null && context.getPlayer().isFallFlying())
			{
				cir.setReturnValue(InteractionResult.PASS);
			}
		}
	}
}
