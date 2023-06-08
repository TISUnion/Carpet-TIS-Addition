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

package carpettisaddition.mixins.rule.entityBrainMemoryUnfreedFix;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<=1.19.4-pre3"))
@Mixin(Entity.class)
public abstract class EntityMixin
{
	@SuppressWarnings("ConstantValue")
	@Inject(
			method = "remove",
			at = @At("TAIL")
	)
	private void entityBrainMemoryUnfreedFix_cleanLivingEntityBrainMemory(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.entityBrainMemoryUnfreedFix)
		{
			if ((Object)this instanceof LivingEntity)
			{
				LivingEntity self = (LivingEntity)(Object)this;
				Map<MemoryModuleType<?>, Optional<?>> memories = ((BrainAccessor)self.getBrain()).getMemories();
				memories.keySet().forEach(lvt1 -> memories.put(lvt1, Optional.empty()));
			}
		}
	}
}
