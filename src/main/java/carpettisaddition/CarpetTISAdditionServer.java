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

package carpettisaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.lifetime.LifeTimeCommand;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.manipulate.ManipulateCommand;
import carpettisaddition.commands.raid.RaidCommand;
import carpettisaddition.commands.raid.RaidTracker;
import carpettisaddition.commands.raycast.RaycastCommand;
import carpettisaddition.commands.refresh.RefreshCommand;
import carpettisaddition.commands.removeentity.RemoveEntityCommand;
import carpettisaddition.commands.scounter.SupplierCounterCommand;
import carpettisaddition.commands.sleep.SleepCommand;
import carpettisaddition.commands.speedtest.SpeedTestCommand;
import carpettisaddition.commands.stop.StopCommandDoubleConfirmation;
import carpettisaddition.commands.xcounter.XpCounterCommand;
import carpettisaddition.helpers.rule.instantBlockUpdaterReintroduced.InstantBlockUpdaterChanger;
import carpettisaddition.helpers.rule.lightEngineMaxBatchSize.LightBatchSizeChanger;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingStandardCarpetLogger;
import carpettisaddition.logging.loggers.phantom.PhantomLogger;
import carpettisaddition.network.TISCMServerPacketHandler;
import carpettisaddition.settings.CarpetRuleRegistrar;
import carpettisaddition.translations.TISAdditionTranslations;
import carpettisaddition.translations.TranslationConstants;
import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Logger;

import java.util.Map;

//#if MC >= 11900
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

//#if MC >= 11600
//$$ import carpet.script.annotation.AnnotationParser;
//$$ import carpettisaddition.script.Functions;
//$$ import carpettisaddition.script.MicroTimingEvent;
//#endif

public class CarpetTISAdditionServer implements CarpetExtension
{
	private static final CarpetTISAdditionServer INSTANCE = new CarpetTISAdditionServer();
	public static final String compactName = CarpetTISAdditionMod.MOD_ID.replace("-","");  // carpettisaddition
	public static final Logger LOGGER = CarpetTISAdditionMod.LOGGER;
	public static MinecraftServer minecraft_server;

	@Override
	public String version()
	{
		return CarpetTISAdditionMod.MOD_ID;
	}

	public static CarpetTISAdditionServer getInstance()
	{
		return INSTANCE;
	}

	public static void init()
	{
		CarpetServer.manageExtension(INSTANCE);
		StackTraceDeobfuscator.fetchMapping();
		TISAdditionTranslations.loadTranslations();

		UpdateSuppressionYeeter.noop();
		StopCommandDoubleConfirmation.noop();
	}

	@Override
	public void onGameStarted()
	{
		// rule description & extras depend on translation
		CarpetRuleRegistrar.register(CarpetServer.settingsManager, CarpetTISAdditionSettings.class);

		// Let's do some logging things
		//#if MC < 11500
		//$$ TISAdditionLoggerRegistry.registerLoggers();
		//#endif

		// scarpet things
		//#if MC >= 11600
		//$$ AnnotationParser.parseFunctionClass(Functions.class);
		//$$ MicroTimingEvent.noop();  //to register event properly
		//#endif
	}

	@Override
	public void onServerLoaded(MinecraftServer server)
	{
		minecraft_server = server;
		CarpetTISAdditionSettings.onWorldLoadingStarted();
	}

	/**
	 * Carpet has issue (bug) to call onServerLoadedWorlds in IntegratedServer, so just do it myself to make sure it works properly
	 * Only in <= 1.15.x
	 * In MC 1.16+ it doesn't need to inject into IntegratedServer class, but we keep our onServerLoadedWorlds hook for easier compat
	 */
	public void onServerLoadedWorlds$TISCM(MinecraftServer server)
	{
		MicroTimingLoggerManager.attachServer(server);
		MicroTimingMarkerManager.getInstance().clear();
		InstantBlockUpdaterChanger.apply();
		LifeTimeTracker.attachServer(server);
		LightBatchSizeChanger.apply();
		LightQueueHUDLogger.getInstance().attachServer(server);
	}

	@Override
	public void onServerClosed(MinecraftServer server)
	{
		LifeTimeTracker.detachServer();
		MicroTimingLoggerManager.detachServer();
		RaidTracker.getInstance().stop();
		SpeedTestCommand.getInstance().onServerClosed();
	}

	@Override
	public void onTick(MinecraftServer server)
	{
		LightQueueHUDLogger.getInstance().tick();
		MicroTimingMarkerManager.getInstance().tick();
		PhantomLogger.getInstance().tick();
	}

	public void onCarpetClientHello(ServerPlayerEntity player)
	{
		MicroTimingStandardCarpetLogger.getInstance().onCarpetClientHello(player);
	}

	@Override
	public void registerCommands(
			CommandDispatcher<ServerCommandSource> dispatcher
			//#if MC >= 11900
			//$$ , CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		CommandTreeContext.Register context = CommandTreeContext.of(
				dispatcher
				//#if MC >= 11900
				//$$ , commandBuildContext
				//#endif
		);
		Lists.newArrayList(
				LifeTimeCommand.getInstance(),
				ManipulateCommand.getInstance(),
				RefreshCommand.getInstance(),
				RaidCommand.getInstance(),
				RaycastCommand.getInstance(),
				RemoveEntityCommand.getInstance(),
				SleepCommand.getInstance(),
				SpeedTestCommand.getInstance(),
				SupplierCounterCommand.getInstance(),
				XpCounterCommand.getInstance()
		).forEach(command ->
				command.registerCommand(context)
		);
	}

	@Override
	public void onPlayerLoggedOut(ServerPlayerEntity player)
	{
		TISCMServerPacketHandler.getInstance().onPlayerDisconnected(player.networkHandler);
		SpeedTestCommand.getInstance().onPlayerDisconnected(player);
	}

	//#if MC >= 11500
	@Override
	public void registerLoggers()
	{
		TISAdditionLoggerRegistry.registerLoggers();
	}

	@Override
	public Map<String, String> canHasTranslations(String lang)
	{
		Map<String, String> trimmedTranslation = Maps.newHashMap();
		String prefix = TranslationConstants.CARPET_TRANSLATIONS_KEY_PREFIX;
		TISAdditionTranslations.getTranslations(lang).forEach((key, value) -> {
			if (key.startsWith(prefix))
			{
				String newKey = key.substring(prefix.length());
				//#if MC >= 11901
				//$$ newKey = "carpet." + newKey;
				//#endif
				trimmedTranslation.put(newKey, value);
			}
		});
		return trimmedTranslation;
	}
	//#endif
}
