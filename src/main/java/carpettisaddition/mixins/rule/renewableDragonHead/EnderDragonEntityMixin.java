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

package carpettisaddition.mixins.rule.renewableDragonHead;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity implements Monster
{
	private boolean flagDropHead;

	public EnderDragonEntityMixin(EntityType<? extends EnderDragonEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(
			method = "<init>",
			at = @At(value = "RETURN")
	)
	private void onConstructed(EntityType<? extends EnderDragonEntity> entityType, World world, CallbackInfo ci)
	{
		this.flagDropHead = false;
	}

	@Inject(
			method = "updatePostDeath",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V"
					//#else
					//$$ target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;remove()V"
					//#endif
			)
	)
	private void dropHead(CallbackInfo ci)
	{
		if (this.flagDropHead)
		{
			this.dropStack(new ItemStack(Items.DRAGON_HEAD));
		}
	}

	@Inject(
			method = "damagePart",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;setHealth(F)V"
			)
	)
	private void onTakingDeathDamage(EnderDragonPart enderDragonPart, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.renewableDragonHead)
		{
			Entity entity = damageSource.getAttacker();
			if (entity instanceof CreeperEntity)
			{
				CreeperEntity creeperEntity = (CreeperEntity) entity;
				if (creeperEntity.shouldDropHead())
				{
					creeperEntity.onHeadDropped();
					this.flagDropHead = true;
				}
			}
		}
	}
}
