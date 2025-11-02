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

package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.helpers.rule.largeBarrel.compat.malilib.LargeBarrelMasaModUtils;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = {
		// Added in malilib (sakura-ryoko's fork) version:
		//   1.21-0.21.6
		//   1.21.3-0.22.4
		//   1.21.4-0.23.1
		@Condition(value = ModIds.malilib, versionPredicates = {
				">=0.21.6 <0.22",
				">=0.22.4 <0.23",
				">=0.23.1",
		}),
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.21")
})
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.interfaces.IDataSyncer")
public interface IDataSyncerMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@ModifyReturnValue(
			method = "getBlockInventory",
			at = @At("RETURN"),
			remap = false
	)
	private Container syncTheOtherSideOfTheLargeBarrel(Container inventory, Level world, BlockPos pos, boolean useNbt)
	{
		if (!useNbt)
		{
			return LargeBarrelMasaModUtils.modifyGetBlockInventoryReturnValueForIDataSyncer(this, inventory, world, pos);
		}
		return inventory;
	}
}
