/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.logger.wanderingTrader;

import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 26.1
//$$ import net.minecraft.world.level.saveddata.WanderingTraderData;
//#endif

@Mixin(WanderingTraderSpawner.class)
public interface WanderingTraderSpawnerAccessor
{
	@Accessor("tickDelay")
	int getTickDelay$TISCM();

	//#if MC >= 26.1
	//$$ @Accessor("traderData")
	//$$ WanderingTraderData getTraderDataDirectly$TISCM();
	//#else
	@Accessor("spawnDelay")
	int getSpawnDelay$TISCM();

	@Accessor("spawnChance")
	int getSpawnChance$TISCM();
	//#endif
}
