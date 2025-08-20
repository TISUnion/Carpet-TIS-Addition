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

package carpettisaddition.commands.lifetime.removal;

import com.google.gson.JsonObject;
import net.minecraft.text.BaseText;

public class LiteralRemovalReason extends RemovalReason
{
	// ============================ Common ============================

	// 32m ~ 128m randomly despawn
	public static final LiteralRemovalReason DESPAWN_RANDOMLY = regular("despawn.randomly");
	// > 128m immediately despawn
	// see carpettisaddition.commands.lifetime.removal.LiteralRemovalReason
	// difficulty peaceful
	public static final LiteralRemovalReason DESPAWN_DIFFICULTY = regular("despawn.difficulty");
	// item/xp orb timeout
	public static final LiteralRemovalReason DESPAWN_TIMEOUT = regular("despawn.timeout");

	// the entity, e.g. creeper, explodes
	public static final LiteralRemovalReason EXPLODED = regular("exploded");

	// for item entity
	public static final LiteralRemovalReason HOPPER = regular("hopper");

	// for item entity and xp orb entity
	public static final LiteralRemovalReason MERGE = regular("merge");

	// the fallback reason
	public static final LiteralRemovalReason OTHER = regular("other");

	// when the persistent tag set to true, treat it as removed since it doesn't count towards mobcaps anymore
	public static final LiteralRemovalReason PERSISTENT = yeetedFromCap("persistent");

	// for 1.16+, enderman
	public static final LiteralRemovalReason PICKUP_BLOCK = yeetedFromCap("pickup_block");

	// player logging out, removing its vehicle
	public static final LiteralRemovalReason PLAYER_LOGOUT = regular("player_logout");

	// for 1.16+, general
	public static final LiteralRemovalReason VEHICLE_MOUNTING = yeetedFromCap("vehicle_mounting");

	// fall down to y=-64 and below
	public static final LiteralRemovalReason VOID = regular("void");

	private final String translationKey;
	private final RemovalType removalType;

	private LiteralRemovalReason(String translationKey, RemovalType removalType)
	{
		this.translationKey = translationKey;
		this.removalType = removalType;
	}

	private static LiteralRemovalReason regular(String translationKey)
	{
		return new LiteralRemovalReason(translationKey, RemovalType.REMOVED_FROM_WORLD);
	}

	// it gets "removed" because it's yeeted from the mobcap
	private static LiteralRemovalReason yeetedFromCap(String translationKey)
	{
		return new LiteralRemovalReason(translationKey, RemovalType.REMOVED_FROM_MOBCAP);
	}

	@Override
	public BaseText toText()
	{
		return tr(this.translationKey);
	}

	@Override
	public String getRecordId()
	{
		return this.translationKey.replace('.', '_');
	}

	@Override
	public JsonObject getRecordData()
	{
		return new JsonObject();
	}

	@Override
	public RemovalType getRemovalType()
	{
		return this.removalType;
	}
}
