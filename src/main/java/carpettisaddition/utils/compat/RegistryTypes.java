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

package carpettisaddition.utils.compat;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * For handling the XxxType -> XxxTypes static var home rename from 26.2-snapshot-3
 * TODO: find a way to let preprocessor to handle it
 * <p>
 * mc1.14 ~ mc26.1: subproject 1.15.2 (main project)        <--------
 * mc26.2+        : subproject 26.2
 */
public class RegistryTypes
{
	public static class Entity
	{
		public static final EntityType<ItemEntity> ITEM = EntityType.ITEM;
		public static final EntityType<Phantom> PHANTOM = EntityType.PHANTOM;
		public static final EntityType<WanderingTrader> WANDERING_TRADER = EntityType.WANDERING_TRADER;
	}

	public static class BlockEntity
	{
		public static final BlockEntityType<BarrelBlockEntity> BARREL = BlockEntityType.BARREL;
	}
}
