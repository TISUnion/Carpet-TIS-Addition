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

package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.text.BaseText;

public class LiteralSpawningReason extends SpawningReason
{
	public static final LiteralSpawningReason NATURAL = regular("natural");
	public static final LiteralSpawningReason PORTAL_PIGMAN = regular("portal_pigman");
	public static final LiteralSpawningReason COMMAND = regular("command");
	public static final LiteralSpawningReason ITEM = regular("item");
	public static final LiteralSpawningReason BLOCK_DROP = regular("block_drop");
	public static final LiteralSpawningReason CONTAINER = regular("container");
	public static final LiteralSpawningReason SLIME = regular("slime");
	public static final LiteralSpawningReason ZOMBIE_REINFORCE = regular("zombie_reinforce");
	public static final LiteralSpawningReason SPAWNER = regular("spawner");
	public static final LiteralSpawningReason RAID = regular("raid");
	public static final LiteralSpawningReason SUMMON = regular("summon");
	public static final LiteralSpawningReason BREEDING = regular("breeding");
	public static final LiteralSpawningReason DISPENSED = regular("dispensed");

	// for 1.16+, entity dismounted from vehicle -> counts towards mobcap
	public static final LiteralSpawningReason VEHICLE_DISMOUNTING = backToCap("vehicle_dismounting");

	// for 1.16+, enderman without block -> counts towards mobcap
	public static final LiteralSpawningReason PLACE_BLOCK = backToCap("place_block");

	private final String translationKey;
	private final SpawningType spawningType;

	private LiteralSpawningReason(String translationKey, SpawningType spawningType)
	{
		this.translationKey = translationKey;
		this.spawningType = spawningType;
	}

	private static LiteralSpawningReason regular(String translationKey)
	{
		return new LiteralSpawningReason(translationKey, SpawningType.ADDED_TO_WORLD);
	}

	// it "spawns" because it's now back to the mobcap
	private static LiteralSpawningReason backToCap(String translationKey)
	{
		return new LiteralSpawningReason(translationKey, SpawningType.ADDED_TO_MOBCAP);
	}

	@Override
	public BaseText toText()
	{
		return tr(this.translationKey);
	}

	@Override
	public SpawningType getSpawningType()
	{
		return this.spawningType;
	}
}
