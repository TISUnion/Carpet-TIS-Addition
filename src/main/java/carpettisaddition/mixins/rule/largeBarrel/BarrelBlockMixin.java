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
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BaseEntityBlock
{
	protected BarrelBlockMixin(Properties settings)
	{
		super(settings);
	}

	@ModifyArg(
			//#if MC >= 12005
			//$$ method = "useWithoutItem",
			//#else
			method = "use",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"
					//#else
					target = "Lnet/minecraft/world/entity/player/Player;openMenu(Lnet/minecraft/world/MenuProvider;)Ljava/util/OptionalInt;"
					//#endif
			)
	)
	private MenuProvider largeBarrel(MenuProvider nameableContainerFactory)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (nameableContainerFactory instanceof BarrelBlockEntity)
			{
				BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) nameableContainerFactory;
				return this.getMenuProvider(barrelBlockEntity.getBlockState(), barrelBlockEntity.getLevel(), barrelBlockEntity.getBlockPos());
			}
		}
		// vanilla
		return nameableContainerFactory;
	}

	/**
	 * Just like {@link net.minecraft.world.level.block.ChestBlock#getMenuProvider}
	 */
	@Nullable
	@Override
	public MenuProvider getMenuProvider (BlockState state, Level world, BlockPos pos)
	{
		MenuProvider vanillaResult = super.getMenuProvider(state, world, pos);
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			return LargeBarrelHelper.getBlockEntitySource(state, world, pos).apply(LargeBarrelHelper.NAME_RETRIEVER).orElse(vanillaResult);
		}
		return vanillaResult;
	}

	@Inject(method = "getAnalogOutputSignal", at = @At("HEAD"), cancellable = true)
	private void getLargeBarrelComparatorOutputMaybe(
			CallbackInfoReturnable<Integer> cir,
			@Local(argsOnly = true) BlockState state,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos
	)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			cir.setReturnValue(AbstractContainerMenu.getRedstoneSignalFromContainer(LargeBarrelHelper.getInventory(state, world, pos)));
		}
	}
}
