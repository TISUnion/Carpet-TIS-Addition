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

package carpettisaddition.mixins.command.info.compat.lithium;

import carpettisaddition.commands.info.QueueAccessibleChunkTickScheduler;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.ReflectionUtils;
import carpettisaddition.utils.mixin.testers.LithiumEntityWorldTickSchedulerTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Queue;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityWorldTickSchedulerTester.class)
})
@Mixin(LevelChunkTicks.class)
public abstract class ChunkTickSchedulerMixin<T> implements QueueAccessibleChunkTickScheduler<T>
{
	@SuppressWarnings("unchecked")
	@Override
	public Queue<ScheduledTick<T>> getTickQueue$TISCM()
	{
		return (Queue<ScheduledTick<T>>)ReflectionUtils.getField(this, "nextTickQueue").orElse(null);
	}
}