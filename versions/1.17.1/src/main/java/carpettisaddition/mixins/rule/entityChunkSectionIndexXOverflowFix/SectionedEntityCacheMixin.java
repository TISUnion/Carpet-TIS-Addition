/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.entityChunkSectionIndexXOverflowFix;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongSortedSet;
import net.minecraft.world.level.entity.EntitySectionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySectionStorage.class)
public abstract class SectionedEntityCacheMixin
{
	@WrapOperation(
			method = {
					"forEachInBox",
					"getSections(II)Lit/unimi/dsi/fastutil/longs/LongSortedSet;"
			},
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/longs/LongSortedSet;subSet(JJ)Lit/unimi/dsi/fastutil/longs/LongSortedSet;",
					remap = false
			),
			remap = true
	)
	private LongSortedSet entityChunkSectionIndexXOverflowFix_safeSubset(LongSortedSet set, long fromElement, long toElement, Operation<LongSortedSet> original)
	{
		if (CarpetTISAdditionSettings.entityChunkSectionIndexXOverflowFix)
		{
			// what we want is [fromElement, toElement)
			if (toElement == Long.MIN_VALUE)  // (uint64_t)toElement == (uint64_t)Long.MAX_VALUE + 1
			{
				// what we want is [fromElement, Long.MAX_VALUE + 1)
				// == [fromElement, Long.MAX_VALUE) | Long.MAX_VALUE
				var result = set.subSet(fromElement, Long.MAX_VALUE);
				if (set.contains(Long.MAX_VALUE))
				{
					var newResult = new LongAVLTreeSet(result);
					newResult.add(Long.MAX_VALUE);
					result = newResult;
				}
				return result;
			}
		}

		// vanilla
		return original.call(set, fromElement, toElement);
	}
}
