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

package carpettisaddition.mixins.command.lifetime.spawning.conversion;

import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.spawning.MobConversionSpawningReason;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 12102
//$$ import carpettisaddition.commands.lifetime.spawning.LiteralSpawningReason;
//$$ import com.llamalad7.mixinextras.sugar.Local;
//$$ import net.minecraft.world.entity.ConversionParams;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(Mob.class)
public abstract class MobEntityMixin extends LivingEntity
{
	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, Level world)
	{
		super(entityType, world);
	}

	@ModifyArg(
			method = "convertTo",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
					//#else
					target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
					//#endif
			)
	)
	private Entity lifetimeTracker_recordSpawning_conversion_mobEntityCommon(
			Entity targetEntity
			//#if MC >= 12102
			//$$ , @Local(argsOnly = true) ConversionParams context
			//#endif
	)
	{
		LifetimeTrackerTarget ltt = (LifetimeTrackerTarget)targetEntity;
		//#if MC >= 12102
		//$$ switch (context.type())
		//$$ {
		//$$ 	case SINGLE -> ltt.recordSpawning(new MobConversionSpawningReason(this.getType()));
		//$$ 	case SPLIT_ON_DEATH -> ltt.recordSpawning(LiteralSpawningReason.SLIME);
		//$$ }
		//#else
		ltt.recordSpawning(new MobConversionSpawningReason(this.getType()));
		//#endif
		return targetEntity;
	}
}