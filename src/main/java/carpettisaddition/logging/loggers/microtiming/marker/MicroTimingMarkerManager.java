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

package carpettisaddition.logging.loggers.microtiming.marker;

//#if MC >= 11500
import carpet.script.utils.ShapeDispatcher;
//#else
//$$ import carpettisaddition.utils.compat.carpet.scarpet.ShapeDispatcher;
//#endif

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MicroTimingMarkerManager extends TranslationContext
{
	private static final MicroTimingMarkerManager INSTANCE = new MicroTimingMarkerManager();

	private final Map<StorageKey, MicroTimingMarker> markers = Maps.newHashMap();

	public MicroTimingMarkerManager()
	{
		super(MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("marker"));
	}

	public static MicroTimingMarkerManager getInstance()
	{
		return INSTANCE;
	}

	public int clear()
	{
		this.cleanMarkersForAll(marker -> true);
		int size = this.markers.size();
		this.markers.clear();
		return size;
	}

	private static boolean checkServerSide(Player playerEntity)
	{
		Level playerWorld = EntityUtils.getEntityWorld(playerEntity);
		return playerEntity instanceof ServerPlayer && !playerWorld.isClientSide() && playerWorld instanceof ServerLevel;
	}

	private void removeMarker(MicroTimingMarker marker)
	{
		marker.cleanShapeToAll();
		this.markers.remove(marker.getStorageKey());
	}

	private void addMarker(MicroTimingMarker marker)
	{
		marker.sendShapeToAll();
		StorageKey key = marker.getStorageKey();
		MicroTimingMarker existedMarker = this.markers.get(key);
		if (existedMarker != null)
		{
			this.removeMarker(existedMarker);
		}
		this.markers.put(marker.getStorageKey(), marker);
	}

	public void addMarker(Player playerEntity, BlockPos blockPos, DyeColor color, @Nullable BaseComponent name)
	{
		if (checkServerSide(playerEntity))
		{
			StorageKey key = new StorageKey(EntityUtils.getEntityWorld(playerEntity), blockPos);
			MicroTimingMarker existedMarker = this.markers.get(key);
			boolean removeExistedMarker = false;
			boolean createNewMarker = false;
			if (existedMarker != null)
			{
				// roll the marker type to the next one
				if (existedMarker.color == color)
				{
					// has next marker type
					if (existedMarker.rollMarkerType())
					{
						Messenger.reminder(playerEntity, tr("on_type_switch", existedMarker.getMarkerType().getFancyString()));
					}
					// no more marker type, remove it
					else
					{
						removeExistedMarker = true;
					}
				}
				// color is different, just remove it and create a new one
				else
				{
					removeExistedMarker = true;
					createNewMarker = true;
				}
			}
			// no existed marker, create a new one
			else
			{
				createNewMarker = true;
			}

			if (removeExistedMarker)
			{
				this.removeMarker(existedMarker);
				Messenger.reminder(playerEntity, tr("on_unmark", existedMarker.toFullText()));
			}
			if (createNewMarker)
			{
				MicroTimingMarker newMarker = new MicroTimingMarker((ServerLevel)EntityUtils.getEntityWorld(playerEntity), blockPos, color, name);
				this.addMarker(newMarker);
				Messenger.reminder(playerEntity, tr("on_mark", newMarker.toFullText()));
			}
		}
	}

	public Optional<DyeColor> getColor(Level world, BlockPos blockPos, MicroTimingMarkerType requiredMarkerType)
	{
		MicroTimingMarker marker = this.markers.get(new StorageKey(world, blockPos));
		if (marker == null)
		{
			return Optional.empty();
		}
		return Optional.ofNullable(marker.getMarkerType().ordinal() >= requiredMarkerType.ordinal() ? marker.color : null);
	}

	public Optional<String> getMarkerName(Level world, BlockPos blockPos)
	{
		return Optional.ofNullable(this.markers.get(new StorageKey(world, blockPos))).map(MicroTimingMarker::getMarkerNameString);
	}

	/*
	 * The marker operators below is more efficient than simply iterating markers and invoking marker's
	 * sendShapeToAll / cleanShapeToAll method, since it's able to send multiple shapes per packet
	 */

	private void sendMarkersForPlayerInner(List<ServerPlayer> playerList, Predicate<MicroTimingMarker> markerPredicate, boolean display)
	{
		if (!playerList.isEmpty() && !this.markers.isEmpty())
		{
			ShapeDispatcher.sendShape(
					playerList,
					this.markers.values().stream().filter(markerPredicate).
							flatMap(marker -> marker.getShapeDataList(display).stream()).
							collect(Collectors.toList())
					//#if MC >= 12005
					//$$ , playerList.get(0).registryAccess()
					//#endif
			);
		}
	}

	public void sendAllMarkersForPlayer(ServerPlayer player)
	{
		this.sendMarkersForPlayerInner(Collections.singletonList(player), marker -> true, true);
	}

	public void cleanAllMarkersForPlayer(ServerPlayer player)
	{
		this.sendMarkersForPlayerInner(Collections.singletonList(player), marker -> true, false);
	}

	public void sendMarkersForAll(Predicate<MicroTimingMarker> markerPredicate)
	{
		this.sendMarkersForPlayerInner(MicroTimingUtil.getSubscribedPlayers(), markerPredicate, true);
	}

	public void cleanMarkersForAll(Predicate<MicroTimingMarker> markerPredicate)
	{
		this.sendMarkersForPlayerInner(MicroTimingUtil.getSubscribedPlayers(), markerPredicate, false);
	}

	/*
	 * marker operators ends
	 */

	/**
	 * When a player switch a server via bungee, the scarpet shapes on the client won't reset since it's not dimension-based
	 * So to make sure the shapes are removable, we don't send shapes with infinite duration, but shapes with limited duration
	 * and send the shapes periodically
	 */
	public void tick()
	{
		if (!CarpetTISAdditionSettings.microTiming)
		{
			return;
		}
		this.sendMarkersForAll(marker -> marker.tickCounter % MicroTimingMarker.MARKER_SYNC_INTERVAL == 0);
		this.markers.values().forEach(marker -> marker.tickCounter++);
	}

	/**
	 * return false if there is not a marker there, true otherwise
	 */
	public boolean tweakMarkerMobility(Player playerEntity, BlockPos blockPos)
	{
		if (checkServerSide(playerEntity))
		{
			StorageKey key = new StorageKey(EntityUtils.getEntityWorld(playerEntity), blockPos);
			MicroTimingMarker marker = this.markers.get(key);
			if (marker != null)
			{
				boolean nextState = !marker.isMovable();
				marker.setMovable(nextState);
				Messenger.reminder(playerEntity, tr(nextState ? "on_mobility_true" : "on_mobility_false", marker.toShortText()));
				return true;
			}
		}
		return false;
	}

	public void moveMarker(Level world, BlockPos blockPos, Direction direction)
	{
		MicroTimingMarker marker = this.markers.get(new StorageKey(world, blockPos));
		if (marker != null && marker.isMovable())
		{
			this.removeMarker(marker);
			this.addMarker(marker.offsetCopy(direction));
		}
	}
}
