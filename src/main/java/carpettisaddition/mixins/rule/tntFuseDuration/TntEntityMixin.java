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

package carpettisaddition.mixins.rule.tntFuseDuration;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.entity.item.PrimedTnt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class TntEntityMixin
{
	@Shadow public abstract void setFuse(int fuse);

	@Inject(
			method = {
					"<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V",
					"<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/entity/LivingEntity;)V"
			},
			at = @At("TAIL")
	)
	private void modifyFuseTimer_tntFuseDuration(CallbackInfo ci)
	{
		this.setFuse(CarpetTISAdditionSettings.tntFuseDuration);
	}

	@ModifyArg(
			method = "defineSynchedData",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/world/entity/item/PrimedTnt;DATA_FUSE_ID:Lnet/minecraft/network/syncher/EntityDataAccessor;"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 12005
					//$$ target = "Lnet/minecraft/network/syncher/SynchedEntityData$Builder;define(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)Lnet/minecraft/network/syncher/SynchedEntityData$Builder;",
					//#else
					target = "Lnet/minecraft/network/syncher/SynchedEntityData;define(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V",
					//#endif
					ordinal = 0
			),
			index = 1
	)
	private Object getCustomFuseTimer_tntFuseDuration(Object oldValue)
	{
		if (oldValue instanceof Integer)
		{
			if (CarpetTISAdditionSettings.tntFuseDuration != CarpetTISAdditionSettings.VANILLA_TNT_FUSE_DURATION)
			{
				return CarpetTISAdditionSettings.tntFuseDuration;
			}
			return oldValue;
		}
		else
		{
			throw new RuntimeException(String.format("getCustomFuseTimer_tntFuseDuration is modifying a wrong argument, oldValue = %s (%s). Please report this bug to Carpet TIS Addition", oldValue, oldValue.getClass()));
		}
	}
}
