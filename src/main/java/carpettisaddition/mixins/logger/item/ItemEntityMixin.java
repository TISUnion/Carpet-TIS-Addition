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

package carpettisaddition.mixins.logger.item;

import carpettisaddition.logging.loggers.entity.ItemLogger;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	private boolean flagDied = false;
	private boolean flagDespawned = false;

	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void onCreated(CallbackInfo ci)
	{
		ItemLogger.getInstance().onEntityCreated((ItemEntity)(Object)this);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=6000"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	void onDespawned(CallbackInfo ci)
	{
		if (!this.flagDespawned)
		{
			ItemLogger.getInstance().onEntityDespawn((ItemEntity)(Object)this);
			this.flagDespawned = true;
		}
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/entity/ItemEntity;discard()V"
					//#else
					target = "Lnet/minecraft/entity/ItemEntity;remove()V"
					//#endif
			)
	)
	void onDied(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		if (!this.flagDied)
		{
			ItemLogger.getInstance().onEntityDied((ItemEntity)(Object)this, source, amount);
			this.flagDied = true;
		}
	}
}
