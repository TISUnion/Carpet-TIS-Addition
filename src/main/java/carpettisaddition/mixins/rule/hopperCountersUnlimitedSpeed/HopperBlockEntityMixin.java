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

package carpettisaddition.mixins.rule.hopperCountersUnlimitedSpeed;

import carpet.CarpetSettings;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.HopperCounterUtils;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.Container;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11700
//$$ import net.minecraft.block.BlockState;
//$$ import net.minecraft.world.World;
//$$ import java.util.function.BooleanSupplier;
//#else
import java.util.function.Supplier;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends RandomizableContainerBlockEntity
{
	private static final int OPERATION_LIMIT = Short.MAX_VALUE;

	//#if MC >= 11700
	//$$ protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	//$$ {
	//$$ 	super(blockEntityType, blockPos, blockState);
	//$$ }
	//#else
	public HopperBlockEntityMixin()
	{
		super(BlockEntityType.HOPPER);
	}
	//#endif

	//#if MC >= 11700
	//$$ @Shadow
	//$$ //#if MC >= 12005
	//$$ //$$ private static boolean insert(World world, BlockPos blockPos, HopperBlockEntity hopper)
	//$$ //#else
	//$$ private static boolean insert(World world, BlockPos blockPos, BlockState blockState, Inventory inventory)
	//$$ //#endif
	//$$ {
	//$$ 	return false;
	//$$ }
	//#else
	@Shadow protected abstract boolean ejectItems();
	@Shadow protected abstract boolean inventoryFull();
	@Shadow protected abstract void setCooldown(int cooldown);
	//#endif

	@Inject(
			method = "tryMoveItems",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V",
					//#else
					target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	//#if MC >= 11700
	//$$ private static void hopperCountersUnlimitedSpeed_impl(World world, BlockPos blockPos, BlockState blockState, HopperBlockEntity hopperBlockEntity, BooleanSupplier booleanSupplier, CallbackInfoReturnable<Boolean> cir)
	//#else
	private void hopperCountersUnlimitedSpeed_impl(Supplier<Boolean> extractMethod, CallbackInfoReturnable<Boolean> cir)
	//#endif
	{
		if (CarpetSettings.hopperCounters && CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			HopperBlockEntity self =
					//#if MC >= 11700
					//$$ hopperBlockEntity;
					//#else
					(HopperBlockEntity)(Object)this;
					//#endif

			// hopper counter check
			DyeColor wool_color = HopperCounterUtils.getWoolColorForHopper(self);
			if (wool_color == null)
			{
				return;
			}
			// hopper counter check ends

			BlockPos hopperPos = self.getBlockPos();

			// unlimited speed
			for (int i = OPERATION_LIMIT - 1; i >= 0; i--)
			{
				boolean flag = false;

				// vanilla copy starts

				//#if MC >= 11700
				//$$ if (!hopperBlockEntity.isEmpty())
				//$$ {
				//$$ 	//#if MC >= 12005
				//$$ 	//$$ flag = insert(world, blockPos, hopperBlockEntity);
				//$$ 	//#else
				//$$ 	flag = insert(world, blockPos, blockState, hopperBlockEntity);
				//$$ 	//#endif
				//$$ }
				//$$ if (!((HopperBlockEntityAccessor)hopperBlockEntity).invokeIsFull())
				//$$ {
				//$$ 	flag |= booleanSupplier.getAsBoolean();
				//$$ }
				//#else
				if (!this.isEmpty())
				{
					flag = this.ejectItems();
				}
				if (!this.inventoryFull())
				{
					flag |= (Boolean)extractMethod.get();
				}
				//#endif
				// vanilla copy ends

				if (!flag)
				{
					break;
				}
				if (i == 0)
				{
					CarpetTISAdditionServer.LOGGER.warn("Hopper at {} exceeded hopperCountersUnlimitedSpeed operation limit {}", hopperPos, OPERATION_LIMIT);
				}
			}

			// no cooldown
			//#if MC >= 11700
			//$$ ((HopperBlockEntityAccessor)hopperBlockEntity).invokeSetCooldown(0);
			//#else
			this.setCooldown(0);
			//#endif
		}
	}

	// to avoid repeatedly extraction an item entity in onEntityCollided
	@Inject(
			method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z",
			at = @At("HEAD"),
			cancellable = true
	)
	private static void hopperCountersUnlimitedSpeed_dontExtractRemovedItem(Container inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.hopperCountersUnlimitedSpeed)
		{
			if (
					//#if MC >= 11700
					//$$ itemEntity.isRemoved()
					//#else
					itemEntity.removed
					//#endif
			)
			{
				cir.setReturnValue(false);
			}
		}
	}
}
