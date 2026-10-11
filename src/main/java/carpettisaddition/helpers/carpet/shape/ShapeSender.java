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

package carpettisaddition.helpers.carpet.shape;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.Value;
import carpettisaddition.utils.EntityUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.Set;

//#if MC >= 1.15
import carpet.network.ServerNetworkHandler;
//#endif

//#if MC < 1.17
import org.apache.commons.lang3.tuple.Pair;
//#endif

public class ShapeSender
{
	private final Set<ServerPlayer> players = Sets.newLinkedHashSet();
	private final Set<ServerLevel> worlds = Sets.newLinkedHashSet();
	private final Set<MinecraftServer> servers = Sets.newLinkedHashSet();
	private final List<ShapeHolder<?>> shapes = Lists.newArrayList();
	private boolean preciseOnly = false;
	private boolean allowVanilla = false;

	private ShapeSender()
	{
	}

	public static ShapeSender create()
	{
		return new ShapeSender();
	}

	public ShapeSender allPlayers(MinecraftServer server)
	{
		this.servers.add(server);
		return this;
	}

	public ShapeSender allowVanilla()
	{
		this.allowVanilla = true;
		return this;
	}

	public ShapeSender player(ServerPlayer player)
	{
		this.players.add(player);
		return this;
	}

	public ShapeSender players(Iterable<? extends ServerPlayer> players)
	{
		players.forEach(this.players::add);
		return this;
	}

	public ShapeSender playersInWorld(ServerLevel world)
	{
		this.worlds.add(world);
		return this;
	}

	public ShapeSender preciseOnly()
	{
		this.preciseOnly = true;
		return this;
	}

	public ShapeSender shape(ShapeHolder<?> shape)
	{
		this.shapes.add(shape);
		return this;
	}

	public ShapeSender shapes(Iterable<? extends ShapeHolder<?>> shapes)
	{
		shapes.forEach(this.shapes::add);
		return this;
	}

	public void send()
	{
		this.sendShapes(true);
	}

	public void clear()
	{
		this.sendShapes(false);
	}

	private void sendShapes(boolean display)
	{
		List<ServerPlayer> recipients = Lists.newArrayList();
		for (ServerPlayer player : this.resolvePlayers())
		{
			if (ShapeSender.isCarpetPlayer(player))
			{
				if (!this.preciseOnly || ShapeUtil.supportsPreciseShapes(player))
				{
					recipients.add(player);
				}
			}
			else if (this.allowVanilla)
			{
				recipients.add(player);
			}
		}
		if (recipients.isEmpty() || this.shapes.isEmpty())
		{
			return;
		}

		List<
				//#if MC >= 1.17
				//$$ ShapeDispatcher.ShapeWithConfig
				//#else
				Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>
				//#endif
				> shapeData = Lists.newArrayList();
		for (ShapeHolder<?> shape : this.shapes)
		{
			shapeData.add(shape.toPair(display));
		}
		ShapeDispatcher.sendShape(recipients, shapeData
				//#if MC >= 1.20.5
				//$$ , recipients.get(0).registryAccess()
				//#endif
		);
	}

	private Set<ServerPlayer> resolvePlayers()
	{
		Set<ServerPlayer> players = Sets.newLinkedHashSet(this.players);
		for (ServerLevel world : this.worlds)
		{
			for (ServerPlayer player : world.getServer().getPlayerList().getPlayers())
			{
				if (EntityUtils.getEntityWorld(player) == world)
				{
					players.add(player);
				}
			}
		}
		for (MinecraftServer server : this.servers)
		{
			players.addAll(server.getPlayerList().getPlayers());
		}
		return players;
	}

	private static boolean isCarpetPlayer(ServerPlayer player)
	{
		//#if MC >= 1.15
		return ServerNetworkHandler.isValidCarpetPlayer(player);
		//#else
		//$$ return false;
		//#endif
	}
}
