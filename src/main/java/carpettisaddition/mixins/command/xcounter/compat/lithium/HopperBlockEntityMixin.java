/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.command.xcounter.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.xcounter.XpCounterUtils;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.mixin.testers.LithiumBlockHopperTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Patch the lithium hopper sleeping, don't allow xp counter hopper to sleep
 * <p>
 * Lithium's HopperBlockEntityMixin uses default priority 1000
 * We need to mixin into its merged method, so here comes priority 2000
 */
@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"),
		@Condition(ModIds.lithium),
		@Condition(type = Condition.Type.TESTER, tester = LithiumBlockHopperTester.class)
})
@Mixin(value = HopperBlockEntity.class, priority = 2000)
public abstract class HopperBlockEntityMixin
{
	@Dynamic("Should be added by lithium lithium.mixin.block.hopper.HopperBlockEntityMixin")
	@Inject(
			method = "checkSleepingConditions",
			at = @At("HEAD"),
			cancellable = true,
			remap = false
	)
	private void hopperXpCounters_xpCountersNeverSleep(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.hopperXpCounters)
		{
			if (XpCounterUtils.isXpCounterHopper((HopperBlockEntity)(Object)this))
			{
				ci.cancel();
			}
		}
	}
}
