/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(targets = "net.minecraft.world.level.block.entity.BarrelBlockEntity$1")
public abstract class BarrelBlockEntityViewerCountManagerMixin
{
	// remap mapping `net.minecraft.world.level.block.entity.BarrelBlockEntity$1 field_27208 this$0` does not work here, idk why
	@Shadow @Final
	BarrelBlockEntity
			//#if MC >= 26.1
			//$$ this$0;
			//#else
			field_27208;
			//#endif

	/**
	 * reference: the lambda {@link net.minecraft.world.level.block.entity.ContainerOpenersCounter} subclass
	 * inside {@link net.minecraft.world.level.block.entity.ChestBlockEntity}
	 */
	@Inject(
			method = "isOwnContainer(Lnet/minecraft/world/entity/player/Player;)Z",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/inventory/ChestMenu;getContainer()Lnet/minecraft/world/Container;"
			),
			cancellable = true
	)
	private void correctLargeBarrelLogic(Player player, CallbackInfoReturnable<Boolean> cir, @Local Container inventory)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			boolean openingLargeBarrel = inventory instanceof CompoundContainer && ((CompoundContainer)inventory).contains(
					//#if MC >= 26.1
					//$$ this.this$0
					//#else
					this.field_27208
					//#endif
			);
			if (openingLargeBarrel)
			{
				cir.setReturnValue(true);
			}
		}
	}
}