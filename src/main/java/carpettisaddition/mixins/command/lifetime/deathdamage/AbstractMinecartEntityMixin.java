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
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// see VehicleEntityMixin for mc1.20.4+
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.20.4"))
@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin implements DamageableEntity
{
	@Unique
	private DamageSource deathDamageSource$TISCM;

	@ModifyVariable(
			method = "hurt",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;ejectPassengers()V"
			),
			argsOnly = true
	)
	private DamageSource lifetimeTracker_recordDeathDamageSource_minecart(DamageSource source)
	{
		this.deathDamageSource$TISCM = source;
		return source;
	}

	@Override
	public DamageSource getDeathDamageSource$TISCM()
	{
		return this.deathDamageSource$TISCM;
	}
}
