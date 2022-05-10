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
import carpettisaddition.script.MicroTimingEvent;
import carpettisaddition.translations.Translator;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.text.MutableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class MicroTimingLoggerManager
{
	private static MicroTimingLoggerManager instance;

	private final Map<ServerWorld, MicroTimingLogger> loggers = new Reference2ObjectArrayMap<>();
	public static final Translator TRANSLATOR = (new AbstractLogger(MicroTimingLogger.NAME){}).getTranslator();
	private TickPhase offWorldTickPhase = new TickPhase(TickStage.UNKNOWN, null);
	public ThreadLocal<ServerWorld> currentWorld = ThreadLocal.withInitial(() -> null);

	// for scarpet event
    public static final Set<BlockPos> trackedPositions = Sets.newHashSet();

	public static MutableText tr(String key, Object... args)
	{
		return TRANSLATOR.tr(key, args);
	}

    public MicroTimingLoggerManager(MinecraftServer minecraftServer)
	{
		for (ServerWorld world : minecraftServer.getWorlds())
		{
			this.loggers.put(world, ((ServerWorldWithMicroTimingLogger)world).getMicroTimingLogger());
		}
	}

	public Map<ServerWorld, MicroTimingLogger> getLoggers()
	{
		return this.loggers;
	}

	public static boolean isLoggerActivated()
	{
		// make sure it's available first
		if (CarpetTISAdditionSettings.microTiming && instance != null)
		{
			// has subscriber
			return TISAdditionLoggerRegistry.__microTiming || !trackedPositions.isEmpty();
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

	private static Optional<MicroTimingLogger> getWorldLogger(World world)
	{
		if (instance != null && world instanceof ServerWorld)
		{
			return Optional.of(((ServerWorldWithMicroTimingLogger)world).getMicroTimingLogger());
		}
		return Optional.empty();
	}

	/*
	 * -------------------------
	 *  General Event Operation
	 * -------------------------
	 */

    public static void dispatchScarpetEvent(World world, BlockPos pos, Supplier<BaseEvent> supplier)
    {
        if (CarpetTISAdditionSettings.microTiming)
        {
            // For scarpet, checking if it's a block tracked by the scarpet bit. Separate from the rest cos idk how to works
            if (trackedPositions.contains(pos))
            {
                MicroTimingEvent.determineBlockEvent(supplier.get(), world, pos);
            }
        }
    }

	private static void onEvent(MicroTimingContext context)
	{
		dispatchScarpetEvent(context.getWorld(), context.getBlockPos(), context.getEventSupplier());
		getWorldLogger(context.getWorld()).ifPresent(logger -> logger.addMessage(context));
	}

	/*
	 * ----------------------------------
	 *  Block Update and Block Operation
	 * ----------------------------------
	 */

	public static void onScheduleBlockUpdate(World world, BlockPos pos, Block sourceBlock, BlockUpdateType updateType, Direction exceptSide)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEventSupplier(() -> new ScheduleBlockUpdateEvent(sourceBlock, updateType, exceptSide)).
						withWoolGetter(MicroTimingUtil::blockUpdateColorGetter)
		);
	}

	public static void onBlockUpdate(World world, BlockPos pos, Block sourceBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEventSupplier(() -> new DetectBlockUpdateEvent(eventType, sourceBlock, updateType, exceptSide)).
						withWoolGetter(MicroTimingUtil::blockUpdateColorGetter)
		);
	}

	public static void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, int flags, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		if (oldState.getBlock() == newState.getBlock())
		{
			// lazy loading
			DyeColor color = null;
			BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, newState.getBlock(), returnValue, flags);

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
				event.addIfChanges(property.getName(), oldState.get(property), newState.get(property));
			}
			if (event.hasChanges())
			{
				onEvent(
						MicroTimingContext.create().
								withWorld(world).withBlockPos(pos).withColor(color).
								withEvent(event)
				);
			}
		}
		else
		{
			onEvent(
					MicroTimingContext.create().
							withWorld(world).withBlockPos(pos).
							withEventSupplier(() -> new BlockReplaceEvent(eventType, oldState.getBlock(), newState.getBlock(), returnValue, flags)).
							withWoolGetter(MicroTimingUtil::defaultColorGetter)
			);
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public static void onExecuteTileTickEvent(World world, OrderedTick<?> tileTickEvent, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		ExecuteTileTickEvent.createFrom(eventType, tileTickEvent).ifPresent(event -> onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(tileTickEvent.pos()).
						withEvent(event)
		));
	}

	public static void onScheduleTileTickEvent(World world, Object object, BlockPos pos, int delay, TickPriority priority, Boolean success)
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

	public static void onExecuteBlockEvent(World world, BlockEvent blockAction, Boolean returnValue, ExecuteBlockEventEvent.FailInfo failInfo, EventType eventType)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(blockAction.pos()).
						withEvent(new ExecuteBlockEventEvent(eventType, blockAction, returnValue, failInfo))
		);
	}

	public static void onScheduleBlockEvent(World world, BlockEvent blockAction, boolean success)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(blockAction.pos()).
						withEvent(new ScheduleBlockEventEvent(blockAction, success))
		);
	}

	/*
	 * ------------------
	 *  Component things
	 * ------------------
	 */

	public static void onEmitBlockUpdate(World world, Block block, BlockPos pos, EventType eventType, String methodName)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new EmitBlockUpdateEvent(eventType, block, methodName))
		);
	}

	public static void onEmitBlockUpdateRedstoneDust(World world, Block block, BlockPos pos, EventType eventType, String methodName, Collection<BlockPos> updateOrder)
	{
		if (!isLoggerActivated())
		{
			return;
		}
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new EmitBlockUpdateRedstoneDustEvent(eventType, block, methodName, pos, updateOrder))
		);
	}

	/*
	 * --------------------
	 *  Tick Stage / Phase
	 * --------------------
	 */

	public static void setTickStage(World world, @NotNull TickStage stage)
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

	public static void setTickStageDetail(World world, String detail)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStageDetail(detail));
	}

	public static void setSubTickStage(World world, AbstractSubStage stage)
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

	public static void setCurrentWorld(ServerWorld world)
	{
		getInstance().currentWorld.set(world);
	}

	@Nullable
	public static ServerWorld getCurrentWorld()
	{
		return getInstance().currentWorld.get();
	}

	@Nullable
	public static TickPhase getOffWorldTickPhase()
	{
		return getInstance().offWorldTickPhase;
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

	public static boolean onPlayerRightClick(PlayerEntity playerEntity, Hand hand, BlockPos blockPos)
	{
		if (MicroTimingUtil.isMarkerEnabled() && playerEntity instanceof ServerPlayerEntity && hand == Hand.MAIN_HAND && MicroTimingUtil.isPlayerSubscribed(playerEntity))
		{
			ItemStack itemStack = playerEntity.getMainHandStack();
			Item holdingItem = itemStack.getItem();
			if (holdingItem instanceof DyeItem)
			{
				MutableText name = null;
				if (itemStack.hasCustomName())
				{
					name = (MutableText)itemStack.getName();
				}
				// server-side check will be in addMarker
				MicroTimingMarkerManager.getInstance().addMarker(playerEntity, blockPos, ((DyeItem)holdingItem).getColor(), name);
				return true;
			}
			if (holdingItem == Items.SLIME_BALL)
			{
				return MicroTimingMarkerManager.getInstance().tweakMarkerMobility(playerEntity, blockPos);
			}
		}
		return false;
	}

	public static void moveMarker(World world, BlockPos blockPos, Direction direction)
	{
		if (MicroTimingUtil.isMarkerEnabled())
		{
			MicroTimingMarkerManager.getInstance().moveMarker(world, blockPos, direction);
		}
	}
}
