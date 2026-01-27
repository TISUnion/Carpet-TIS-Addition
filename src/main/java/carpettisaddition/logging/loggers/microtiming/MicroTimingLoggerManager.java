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

package carpettisaddition.logging.loggers.microtiming;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.*;
import carpettisaddition.logging.loggers.microtiming.interfaces.ServerWorldWithMicroTimingLogger;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.logging.loggers.microtiming.tickphase.TickPhase;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.AbstractSubStage;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingContext;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.ItemUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.TickNextTickData;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//#if MC >= 26.1
//$$ import net.minecraft.core.component.DataComponents;
//#endif

//#if MC >= 11600
//$$ import carpettisaddition.script.MicroTimingEvent;
//$$ import com.google.common.collect.Sets;
//$$ import java.util.Set;
//$$ import java.util.function.Supplier;
//#endif

public class MicroTimingLoggerManager
{
	private static MicroTimingLoggerManager instance;

	private final Map<ServerLevel, MicroTimingLogger> loggers = new Reference2ObjectArrayMap<>();
	public static final Translator TRANSLATOR = (new AbstractLogger(MicroTimingLogger.NAME, false){}).getTranslator();
	private TickPhase offWorldTickPhase = new TickPhase(TickStage.UNKNOWN, null);
	private final ThreadLocal<ServerLevel> currentWorld = ThreadLocal.withInitial(() -> null);

	//#if MC >= 11600
	//$$ // for scarpet event
	//$$ public static final Set<BlockPos> trackedPositions = Sets.newHashSet();
	//#endif

	public static BaseComponent tr(String key, Object... args)
	{
		return TRANSLATOR.tr(key, args);
	}

	public MicroTimingLoggerManager(MinecraftServer minecraftServer)
	{
		for (ServerLevel world : minecraftServer.getAllLevels())
		{
			this.loggers.put(world, ((ServerWorldWithMicroTimingLogger)world).getMicroTimingLogger$TISCM());
		}
	}

	public Map<ServerLevel, MicroTimingLogger> getLoggers()
	{
		return this.loggers;
	}

	public static boolean isLoggerActivated()
	{
		// make sure it's available first
		if (CarpetTISAdditionSettings.microTiming && instance != null)
		{
			//#if MC >= 11600
			//$$ if (!trackedPositions.isEmpty())
			//$$ {
			//$$ 	return true;
			//$$ }
			//#endif

			// has subscriber
			return TISAdditionLoggerRegistry.__microTiming;
		}
		return false;
	}

	public static void attachServer(MinecraftServer minecraftServer)
	{
		instance = new MicroTimingLoggerManager(minecraftServer);
	}

	public static void detachServer()
	{
		instance = null;
	}

	@NotNull
	public static MicroTimingLoggerManager getInstance()
	{
		if (instance == null)
		{
			throw new RuntimeException("MicroTimingLoggerManager not attached");
		}
		return instance;
	}

	public static Optional<MicroTimingLoggerManager> getInstanceOptional()
	{
		return Optional.ofNullable(instance);
	}

	private static Optional<MicroTimingLogger> getWorldLogger(Level world)
	{
		if (instance != null && world instanceof ServerLevel)
		{
			return Optional.of(((ServerWorldWithMicroTimingLogger)world).getMicroTimingLogger$TISCM());
		}
		return Optional.empty();
	}

	/*
	 * -------------------------
	 *  General Event Operation
	 * -------------------------
	 */

	//#if MC >= 11600
	//$$ public static void dispatchScarpetEvent(Level world, BlockPos pos, Supplier<BaseEvent> supplier)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.microTiming)
	//$$ 	{
	//$$ 		// For scarpet, checking if it's a block tracked by the scarpet bit. Separate from the rest cos idk how to works
	//$$ 		if (trackedPositions.contains(pos))
	//$$ 		{
	//$$ 			MicroTimingEvent.determineBlockEvent(supplier.get(), world, pos);
	//$$ 		}
	//$$ 	}
	//$$ }
	//#endif

	private static void onEvent(MicroTimingContext context)
	{
		//#if MC >= 11600
		//$$ dispatchScarpetEvent(context.getWorld(), context.getBlockPos(), context.getEventSupplier());
		//#endif

		getWorldLogger(context.getWorld()).ifPresent(logger -> logger.addMessage(context));
	}

	/*
	 * ----------------------------------
	 *  Block Update and Block Operation
	 * ----------------------------------
	 */

	public static void onScheduleBlockUpdate(Level world, BlockPos pos, Block sourceBlock, BlockUpdateType updateType, Direction exceptSide)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(pos).
				withEventSupplier(() -> new ScheduleBlockUpdateEvent(sourceBlock, updateType, exceptSide)).
				withWoolGetter(MicroTimingUtil::blockUpdateColorGetter)
		);
	}

	public static void onBlockUpdate(Level world, BlockPos pos, Block sourceBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(pos).
				withEventSupplier(() -> new DetectBlockUpdateEvent(eventType, sourceBlock, updateType, exceptSide)).
				withWoolGetter(MicroTimingUtil::blockUpdateColorGetter)
		);
	}
	
	public static void onSetBlockState(Level world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, int flags, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		if (oldState.getBlock() == newState.getBlock())
		{
			// lazy loading
			DyeColor color = null;
			List<BlockStateChangeEvent.PropertyChange> changes = Lists.newArrayList();

			for (Property<?> property: newState.getProperties())
			{
				if (color == null)
				{
					Optional<DyeColor> optionalDyeColor = MicroTimingUtil.defaultColorGetter(world, pos);
					if (!optionalDyeColor.isPresent())
					{
						break;
					}
					color = optionalDyeColor.get();
				}
				if (oldState.getValue(property) != newState.getValue(property))
				{
					changes.add(new BlockStateChangeEvent.PropertyChange(property, oldState.getValue(property), newState.getValue(property)));
				}
			}
			if (!changes.isEmpty())
			{
				onEvent(MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).withColor(color).
						withEventSupplier(() -> {
							BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, oldState, newState, returnValue, flags);
							event.setChanges(changes);
							return event;
						})
				);
			}
		}
		else
		{
			onEvent(MicroTimingContext.create().
					withWorld(world).withBlockPos(pos).
					withEventSupplier(() -> new BlockReplaceEvent(eventType, oldState, newState, returnValue, flags)).
					withWoolGetter(MicroTimingUtil::defaultColorGetter)
			);
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public static void onExecuteTileTickEvent(Level world, TickNextTickData<?> tileTickEvent, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}

		BlockPos pos =
				//#if MC >= 11800
				//$$ tileTickEvent.pos();
				//#else
				tileTickEvent.pos;
				//#endif

		ExecuteTileTickEvent.createFrom(eventType, tileTickEvent).ifPresent(event -> onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(event)
		));
	}

	public static void onScheduleTileTickEvent(Level world, Object object, BlockPos pos, int delay, TickPriority priority, Boolean success)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		EventSource.fromObject(object).ifPresent(eventSource -> onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new ScheduleTileTickEvent(eventSource, delay, priority, success))
		));
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	public static void onExecuteBlockEvent(Level world, BlockEventData blockAction, Boolean returnValue, ExecuteBlockEventEvent.FailInfo failInfo, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(blockAction.getPos()).
				withEvent(new ExecuteBlockEventEvent(eventType, blockAction, returnValue, failInfo))
		);
	}

	public static void onScheduleBlockEvent(Level world, BlockEventData blockAction, boolean success)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(blockAction.getPos()).
				withEvent(new ScheduleBlockEventEvent(blockAction, success))
		);
	}

	/*
	 * ------------------
	 *  Component things
	 * ------------------
	 */

	public static void onEmitBlockUpdate(Level world, Block block, BlockPos pos, EventType eventType, String methodName)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(pos).
				withEvent(new EmitBlockUpdateEvent(eventType, block, methodName))
		);
	}

	public static void onEmitBlockUpdateRedstoneDust(Level world, Block block, BlockPos pos, EventType eventType, String methodName, Collection<BlockPos> updateOrder)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(pos).
				withEvent(new EmitBlockUpdateRedstoneDustEvent(eventType, block, methodName, pos, updateOrder))
		);
	}

	public static void onPistonComputePushStructureEvent(Level world, BlockPos pos, Block block, boolean success, PistonStructureResolver resolver)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(MicroTimingContext.create().
				withWorld(world).withBlockPos(pos).
				withEvent(new PistonComputePushStructureEvent(DimensionWrapper.of(world), block, PistonComputePushStructureEvent.Result.from(success, resolver)))
		);
	}

	/*
	 * --------------------
	 *  Tick Stage / Phase
	 * --------------------
	 */

	public static void setTickStage(Level world, @NotNull TickStage stage)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStage(stage));
	}

	public static void setTickStage(@NotNull TickStage stage)
	{
		if (instance != null)
		{
			for (MicroTimingLogger logger : instance.loggers.values())
			{
				logger.setTickStage(stage);
			}
			instance.offWorldTickPhase = instance.offWorldTickPhase.withMainStage(stage);
		}
	}

	public static void setTickStageDetail(Level world, @Nullable String detail)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStageDetail(detail));
	}

	public static void setSubTickStage(Level world, AbstractSubStage stage)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setSubTickStage(stage));
	}

	public static void setSubTickStage(AbstractSubStage stage)
	{
		if (instance != null)
		{
			for (MicroTimingLogger logger : instance.loggers.values())
			{
				logger.setSubTickStage(stage);
			}
			instance.offWorldTickPhase = instance.offWorldTickPhase.withSubStage(stage);
		}
	}

	private synchronized void flush()
	{
		for (MicroTimingLogger logger : this.loggers.values())
		{
			logger.flushMessages();
		}
	}

	/*
	 * ----------------
	 *     For API
	 * ----------------
	 */

	public static void setCurrentWorld(ServerLevel world)
	{
		getInstanceOptional().ifPresent(mgr -> mgr.currentWorld.set(world));
	}

	@Nullable
	public static ServerLevel getCurrentWorld()
	{
		return getInstanceOptional().map(mgr -> mgr.currentWorld.get()).orElse(null);
	}

	@Nullable
	public static TickPhase getOffWorldTickPhase()
	{
		return getInstanceOptional().map(mgr -> mgr.offWorldTickPhase).orElse(null);
	}

	/**
	 * Invoke this at the end of a "gametick", or right before the first thing in the next "gametick" happens
	 */
	public static void flushMessages()
	{
		if (instance != null && isLoggerActivated())
		{
			instance.flush();
		}
	}

	/*
	 * ----------------
	 *   Marker Logic
	 * ----------------
	 */

	public static boolean onPlayerRightClick(Player playerEntity, InteractionHand hand, BlockPos blockPos)
	{
		if (MicroTimingUtil.isMarkerEnabled() && playerEntity instanceof ServerPlayer && hand == InteractionHand.MAIN_HAND && MicroTimingUtil.isPlayerSubscribed(playerEntity))
		{
			ItemStack itemStack = playerEntity.getMainHandItem();
			Item holdingItem = itemStack.getItem();
			if (holdingItem instanceof DyeItem)
			{
				BaseComponent name = null;
				if (ItemUtils.hasCustomName(itemStack))
				{
					name = (BaseComponent)itemStack.getHoverName();
				}
				//#if MC >= 26.1
				//$$ DyeColor dyeColor = itemStack.get(DataComponents.DYE);
				//#else
				DyeColor dyeColor = ((DyeItem)holdingItem).getDyeColor();
				//#endif
				if (dyeColor != null)
				{
					// the server-side-only check will be in addMarker
					MicroTimingMarkerManager.getInstance().addMarker(playerEntity, blockPos, dyeColor, name);
				}
				return true;
			}
			if (holdingItem == Items.SLIME_BALL)
			{
				return MicroTimingMarkerManager.getInstance().tweakMarkerMobility(playerEntity, blockPos);
			}
		}
		return false;
	}

	public static void moveMarker(Level world, BlockPos blockPos, Direction direction)
	{
		if (MicroTimingUtil.isMarkerEnabled())
		{
			MicroTimingMarkerManager.getInstance().moveMarker(world, blockPos, direction);
		}
	}
}
