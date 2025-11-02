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

package carpettisaddition.logging.loggers.mobcapsLocal;

import carpet.logging.HUDLogger;
import carpet.utils.SpawnReporter;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.mixins.logger.mobcapsLocal.SpawnDensityCapperAccessor;
import carpettisaddition.mixins.logger.mobcapsLocal.SpawnDensityCapperDensityCapAccessor;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.LocalMobCapCalculator;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

//#if MC >= 11900
//$$ import net.minecraft.network.chat.Component;
//#endif

public class MobcapsLocalLogger extends AbstractHUDLogger
{
	public static final String NAME = "mobcapsLocal";
	private static final MobcapsLocalLogger INSTANCE = new MobcapsLocalLogger();

	private final Map<DimensionWrapper, LocalMobCapCalculator> capperMap = Maps.newHashMap();
	private final ThreadLocal<@Nullable Object2IntMap<MobCategory>> mobcapsMap = ThreadLocal.withInitial(() -> null);

	private MobcapsLocalLogger()
	{
		super(NAME, true);
	}

	public static MobcapsLocalLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		if (option != null)
		{
			ServerPlayer specifiedPlayer = CarpetTISAdditionServer.minecraft_server.getPlayerList().getPlayerByName(option);
			if (specifiedPlayer != null)
			{
				playerEntity = specifiedPlayer;
			}
			else
			{
				return new BaseComponent[]{Messenger.formatting(tr("player_not_found", option), ChatFormatting.GRAY)};
			}
		}
		if (playerEntity instanceof ServerPlayer)
		{
			ServerPlayer serverPlayerEntity = (ServerPlayer)playerEntity;
			final BaseComponent result = Messenger.c("g [", Messenger.formatting(tr("local"), "g"), "g ] ");
			this.withLocalMobcapContext(
					serverPlayerEntity,
					() -> {
						//#if MC >= 11900
						//$$ List<Component> lines =
						//#else
						List<BaseComponent> lines =
						//#endif
								SpawnReporter.printMobcapsForDimension(serverPlayerEntity.getLevel(), false);

						result.append(lines.get(0));
						if (option != null)
						{
							result.append(Messenger.s(String.format(" (%s)", option), "g"));
						}
					},
					() -> {
						result.append("-- Not available --");
					}
			);
			return new BaseComponent[]{result};
		}
		return new BaseComponent[]{Messenger.s("-- Not ServerPlayerEntity --")};
	}

	@Override
	public HUDLogger createCarpetLogger()
	{
		return new HUDLogger(TISAdditionLoggerRegistry.getLoggerField(NAME), NAME, null, null, false) {
			@Override
			public String[] getOptions()
			{
				return CarpetTISAdditionServer.minecraft_server.getPlayerNames();
			}
		};
	}

	public void setCapper(DimensionWrapper dim, LocalMobCapCalculator capper)
	{
		this.capperMap.put(dim, capper);
	}

	public void onServerClosed()
	{
		this.capperMap.clear();
	}

	@Nullable
	public Object2IntMap<MobCategory> getMobcapsMap()
	{
		return mobcapsMap.get();
	}

	public void withLocalMobcapContext(ServerPlayer player, Runnable runnable, Runnable failureCallback)
	{
		LocalMobCapCalculator capper = this.capperMap.get(DimensionWrapper.of(player));
		if (capper != null)
		{
			LocalMobCapCalculator.MobCounts cap = ((SpawnDensityCapperAccessor)capper).getPlayersToDensityCap().getOrDefault(player, SpawnDensityCapperDensityCapAccessor.invokeConstructor());
			this.mobcapsMap.set(((SpawnDensityCapperDensityCapAccessor) cap).getSpawnGroupsToDensity());
			try
			{
				runnable.run();
			}
			finally
			{
				this.mobcapsMap.set(null);
			}
		}
		else
		{
			failureCallback.run();
		}
	}
}