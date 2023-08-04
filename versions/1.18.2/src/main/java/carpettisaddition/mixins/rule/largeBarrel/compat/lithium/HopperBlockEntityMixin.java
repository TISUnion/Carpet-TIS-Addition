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

package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(ModIds.lithium))
@Mixin(value = HopperBlockEntity.class, priority = 2000)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	{
		super(blockEntityType, blockPos, blockState);
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Dynamic("add by lithium")
	@Inject(method = "cacheInsertBlockInventory", at = @At("HEAD"))
	private void cacheInsertBlockInventory_callback(Inventory inventory, CallbackInfo ci)
	{
		Messenger.broadcast(Messenger.format("hopper %s cacheInsertBlockInventory %s", this.pos, inventory));
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Dynamic("add by lithium")
	@Inject(method = "handleInventoryRemoved", at = @At("HEAD"))
	private void handleInventoryRemoved_callback(Inventory inventory, CallbackInfo ci)
	{
		Messenger.broadcast(Messenger.format("hopper %s handleInventoryRemoved %s", this.pos, inventory));
	}
}
