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

package carpettisaddition.mixins.command.lifetime.deathdamage;

import carpettisaddition.commands.lifetime.interfaces.DamageableEntity;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.20.4"))
@Mixin(VehicleEntity.class)
public abstract class VehicleEntityMixin implements DamageableEntity
{
	private DamageSource deathDamageSource;

	@ModifyVariable(
			method = "hurt",
			at =@At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;discard()V"
			),
			argsOnly = true
	)
	private DamageSource lifetimeTracker_recordDeathDamageSource_vehicle1(DamageSource source)
	{
		this.deathDamageSource = source;
		return source;
	}

	@ModifyVariable(
			method = "hurt",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;)V"
					//#else
					target = "Lnet/minecraft/world/entity/vehicle/VehicleEntity;destroy(Lnet/minecraft/world/damagesource/DamageSource;)V"
					//#endif
			),
			argsOnly = true
	)
	private DamageSource lifetimeTracker_recordDeathDamageSource_vehicle2(DamageSource source)
	{
		this.deathDamageSource = source;
		return source;
	}

	@Override
	public DamageSource getDeathDamageSource()
	{
		return this.deathDamageSource;
	}
}
