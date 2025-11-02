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

package carpettisaddition.mixins.command.xcounter;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.xcounter.XpCounterHopperTicker;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11700
//$$ import com.llamalad7.mixinextras.sugar.Local;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	@Inject(
			method = "tryMoveItems",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;isEmpty()Z",
					//#else
					//$$ target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;inventoryEmpty()Z",
					//#endif
					ordinal = 0
			)
	)
	private
	//#if MC >= 11700
	//$$ static
	//#endif
	void hopperXpCounter_tick(
			CallbackInfoReturnable<Boolean> cir
			//#if MC >= 11700
			//$$ , @Local(argsOnly = true) HopperBlockEntity hopperBlockEntity
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.hopperXpCounters)
		{
			//#if MC < 11700
			HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)(Object)this;
			//#endif
			XpCounterHopperTicker.tickHopper(hopperBlockEntity);
		}
	}
}
