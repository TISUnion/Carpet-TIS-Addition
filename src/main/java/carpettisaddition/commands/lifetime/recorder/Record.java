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

package carpettisaddition.commands.lifetime.recorder;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.commands.lifetime.utils.AbstractReason;
import carpettisaddition.utils.GameUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import carpettisaddition.utils.gson.IdentifierAdaptor;
import carpettisaddition.utils.gson.Vec3dAdaptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class Record
{
	public long serverTick;
	public long gameTime;

	public String eventType;
	public String eventId;
	public Vec3 eventPosition;
	public JsonElement eventData;

	public ResourceLocation entityType;
	public int entityId;
	public UUID entityUuid;
	public Vec3 entityPosition;
	public ResourceLocation entityDimension;
	public long entityLifetime;

	public enum EventType
	{
		SPAWNING, REMOVAL;

		public static EventType fromReason(AbstractReason reason)
		{
			if (reason instanceof SpawningReason)
			{
				return SPAWNING;
			}
			else if (reason instanceof RemovalReason)
			{
				return REMOVAL;
			}
			else
			{
				throw new IllegalArgumentException("unknown reason " + reason);
			}
		}
	}

	public static Record create(Entity entity, EventType eventType, String eventId, JsonElement eventData)
	{
		Record r = new Record();
		LifetimeTrackerTarget ltt = (LifetimeTrackerTarget)entity;
		r.serverTick = CarpetTISAdditionServer.minecraft_server.getTickCount();
		r.gameTime = GameUtils.getGameTime();

		r.eventId = eventId;
		r.eventType = eventType.name().toLowerCase();
		r.eventPosition = eventType == EventType.SPAWNING ? ltt.getSpawningPosition() : ltt.getRemovalPosition();
		r.eventData = eventData;

		r.entityType = EntityType.getKey(entity.getType());
		//#if MC >= 1.17.0
		//$$ r.entityId = entity.getId();
		//#else
		r.entityId = entity.getId();
		//#endif
		r.entityUuid = entity.getUUID();
		r.entityPosition = entity.position();
		r.entityDimension = DimensionWrapper.of(entity).getIdentifier();
		r.entityLifetime = eventType == EventType.SPAWNING ? 0 : ltt.getLifeTime();
		return r;
	}

	public static final Gson gson = new GsonBuilder().
			disableHtmlEscaping().
			registerTypeAdapter(ResourceLocation.class, new IdentifierAdaptor()).
			registerTypeAdapter(Vec3.class, new Vec3dAdaptor()).
			create();

	public String toJson()
	{
		return gson.toJson(this);
	}
}
