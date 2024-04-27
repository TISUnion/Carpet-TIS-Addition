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
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrelBlock.class)
public abstract class BarrelBlockMixin extends BlockWithEntity
{
	protected BarrelBlockMixin(Settings settings)
	{
		super(settings);
	}

	@ModifyArg(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					target = "Lnet/minecraft/entity/player/PlayerEntity;openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;"
					//#else
					//$$ target = "Lnet/minecraft/entity/player/PlayerEntity;openContainer(Lnet/minecraft/container/NameableContainerFactory;)Ljava/util/OptionalInt;"
					//#endif
			)
	)
	private NamedScreenHandlerFactory largeBarrel(NamedScreenHandlerFactory nameableContainerFactory)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (nameableContainerFactory instanceof BarrelBlockEntity)
			{
				BarrelBlockEntity barrelBlockEntity = (BarrelBlockEntity) nameableContainerFactory;
				return this.
						//#if MC >= 11600
						createScreenHandlerFactory
						//#else
						//$$ createContainerFactory
						//#endif
								(barrelBlockEntity.getCachedState(), barrelBlockEntity.getWorld(), barrelBlockEntity.getPos());
			}
		}
		// vanilla
		return nameableContainerFactory;
	}

	/**
	 * (<=1.15) Just like {@link net.minecraft.block.ChestBlock#createContainerFactory}
	 * (>=1.16) Just like {@link net.minecraft.block.ChestBlock#createScreenHandlerFactory}
	 */
	@Nullable
	@Override
	public NamedScreenHandlerFactory
	//#if MC >= 11600
	createScreenHandlerFactory
	//#else
	//$$ createContainerFactory
	//#endif
	(BlockState state, World world, BlockPos pos)
	{
		NamedScreenHandlerFactory vanillaResult = super.
				//#if MC >= 11600
				createScreenHandlerFactory
				//#else
				//$$ createContainerFactory
				//#endif
						(state, world, pos);
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			return LargeBarrelHelper.getBlockEntitySource(state, world, pos).apply(LargeBarrelHelper.NAME_RETRIEVER).orElse(vanillaResult);
		}
		return vanillaResult;
	}

	@Inject(method = "getComparatorOutput", at = @At("HEAD"), cancellable = true)
	private void getLargeBarrelComparatorOutputMaybe(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			cir.setReturnValue(ScreenHandler.calculateComparatorOutput(LargeBarrelHelper.getInventory(state, world, pos)));
		}
	}
}
