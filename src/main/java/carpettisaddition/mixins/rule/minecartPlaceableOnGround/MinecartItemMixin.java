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

package carpettisaddition.mixins.rule.minecartPlaceableOnGround;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.minecartPlaceableOnGround.MinecartPlaceableOnGroundImpl;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.InteractionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12102
//$$ import net.minecraft.entity.EntityType;
//#endif

@Mixin(MinecartItem.class)
public abstract class MinecartItemMixin
{
	@Shadow @Final
	//#if MC >= 12102
	//$$ private EntityType<? extends AbstractMinecartEntity>
	//#else
	private AbstractMinecart.Type
	//#endif
			type;

	@ModifyReturnValue(
			method = "useOn",
			at = @At(
					value = "RETURN",
					ordinal = 0
			)
	)
	private InteractionResult minecartPlaceableOnGround_hook(
			InteractionResult actionResult,
			@Local(argsOnly = true) UseOnContext context
	)
	{
		if (CarpetTISAdditionSettings.minecartPlaceableOnGround)
		{
			// it should be FAIL for vanilla
			// if not, in case other mod is doing their thing, then cancel the rule impl for safety
			if (actionResult == InteractionResult.FAIL)
			{
				actionResult = MinecartPlaceableOnGroundImpl.placeMinecartOnGround(context, this.type);
			}
		}
		return actionResult;
	}
}
