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

package carpettisaddition.mixins.logger.mobcapsLocal;

import carpet.utils.SpawnReporter;
import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	@Shadow(remap = false) @Final public static int MAGIC_NUMBER;

	@Unique
	private static MobCategory currentSpawnGroup$TISCM = null;

	@ModifyVariable(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/ArrayList;<init>()V",
					remap = false
			),
			ordinal = 0,
			remap = false
	)
	private static int applyMobcapsLocalLoggerValueOverrideForMaxMobLimit(int chunkcount)
	{
		Object2IntMap<MobCategory> mobcapsMap = MobcapsLocalLogger.getInstance().getMobcapsMap();
		if (mobcapsMap != null && currentSpawnGroup$TISCM != null)
		{
			chunkcount = MAGIC_NUMBER;
		}
		return chunkcount;
	}

	@ModifyArg(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getOrDefault(Ljava/lang/Object;I)I",
					remap = false
			),
			index = 0,
			remap = false
	)
	private static Object storeCurrentSpawnGroup(Object spawnGroup)
	{
		if (spawnGroup instanceof MobCategory)
		{
			currentSpawnGroup$TISCM = (MobCategory)spawnGroup;
		}
		return spawnGroup;
	}

	@ModifyExpressionValue(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/Object2IntMap;getOrDefault(Ljava/lang/Object;I)I",
					ordinal = 0
			),
			remap = false
	)
	private static int applyMobcapsLocalLoggerValueOverrideForCurrentMobCount(int cur)
	{
		Object2IntMap<MobCategory> mobcapsMap = MobcapsLocalLogger.getInstance().getMobcapsMap();
		if (mobcapsMap != null && currentSpawnGroup$TISCM != null)
		{
			cur = mobcapsMap.getOrDefault(currentSpawnGroup$TISCM, -1);
		}
		return cur;
	}
}