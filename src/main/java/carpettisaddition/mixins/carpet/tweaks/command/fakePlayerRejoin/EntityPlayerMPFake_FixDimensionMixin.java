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

package carpettisaddition.mixins.carpet.tweaks.command.fakePlayerRejoin;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.helpers.carpet.tweaks.command.fakePlayerRejoin.FakePlayerRejoinHelper;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC <= 11500
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.dimension.DimensionType;
//#endif

@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFake_FixDimensionMixin
{
	//#if MC <= 11500
	@ModifyExpressionValue(
			method = "createFake",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/patches/EntityPlayerMPFake;dimension:Lnet/minecraft/world/level/dimension/DimensionType;",
					ordinal = 0
			)
	)
	private static DimensionType fakePlayerRejoin_dontDoTransdimensionTeleport(
			DimensionType playerDimension,
			@Local(argsOnly = true) DimensionType targetDimension
	)
	{
		if (FakePlayerRejoinHelper.isRejoin.get())
		{
			playerDimension = targetDimension;
		}
		return playerDimension;
	}
	//#endif

	// TODO
}
