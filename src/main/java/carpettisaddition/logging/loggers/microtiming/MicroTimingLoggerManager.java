package carpettisaddition.logging.loggers.microtiming;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.events.*;
import carpettisaddition.logging.loggers.microtiming.interfaces.IServerWorld;
import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.logging.loggers.microtiming.tickstages.TickStageExtraBase;
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
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
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
	public static final Translator TRANSLATOR = (new MicroTimingLogger(null)).getTranslator();
	private long lastFlushTime;

	// for scarpet event
    public static Set<BlockPos> trackedPositions = Sets.newHashSet();

    public MicroTimingLoggerManager(MinecraftServer minecraftServer)
	{
		this.lastFlushTime = -1;
		for (ServerWorld world : minecraftServer.getWorlds())
		{
			this.loggers.put(world, ((IServerWorld)world).getMicroTimingLogger());
		}
	}

	@Nullable
	public static MicroTimingLoggerManager getInstance()
	{
		return instance;
	}

	public Map<ServerWorld, MicroTimingLogger> getLoggers()
	{
		return this.loggers;
	}

	public static boolean isLoggerActivated()
	{
		return CarpetTISAdditionSettings.microTiming && TISAdditionLoggerRegistry.__microTiming && instance != null;
	}

	public static void attachServer(MinecraftServer minecraftServer)
	{
		instance = new MicroTimingLoggerManager(minecraftServer);
		CarpetTISAdditionServer.LOGGER.debug("Attached MicroTick loggers to " + instance.loggers.size() + " worlds");
	}

	public static void detachServer()
	{
		instance = null;
		CarpetTISAdditionServer.LOGGER.debug("Detached MicroTick loggers");
	}

	private static Optional<MicroTimingLogger> getWorldLogger(World world)
	{
		if (instance != null && world instanceof ServerWorld)
		{
			return Optional.of(((IServerWorld)world).getMicroTimingLogger());
		}
		return Optional.empty();
	}

	public static String tr(String key, String text, boolean autoFormat)
	{
		return TRANSLATOR.tr(key, text, autoFormat);
	}

	public static String tr(String key, String text)
	{
		return TRANSLATOR.tr(key, text);
	}

	public static String tr(String key)
	{
		return TRANSLATOR.tr(key);
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
		if (isLoggerActivated())
		{
			dispatchScarpetEvent(context.getWorld(), context.getBlockPos(), context.getEventSupplier());
			getWorldLogger(context.getWorld()).ifPresent(logger -> logger.addMessage(context));
		}
	}

	/*
	 * ----------------------------------
	 *  Block Update and Block Operation
	 * ----------------------------------
	 */

	public static void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Direction exceptSide, EventType eventType)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEventSupplier(() -> new DetectBlockUpdateEvent(eventType, fromBlock, updateType, () -> updateType.getUpdateOrderList(exceptSide))).
						withWoolGetter(MicroTimingUtil::blockUpdateColorGetter)
		);
	}

	public static void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, int flags, EventType eventType)
	{
		if (isLoggerActivated())
		{
			if (oldState.getBlock() == newState.getBlock())
			{
				// lazy loading
				DyeColor color = null;
				BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, returnValue, newState.getBlock(), flags);

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
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public static void onExecuteTileTickEvent(World world, ScheduledTick<Block> event, EventType eventType)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(event.pos).
						withEvent(new ExecuteTileTickEvent(eventType, event))
		);
	}

	public static void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority, Boolean success)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new ScheduleTileTickEvent(block, pos, delay, priority, success))
		);
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	public static void onExecuteBlockEvent(World world, BlockEvent blockAction, Boolean returnValue, ExecuteBlockEventEvent.FailInfo failInfo, EventType eventType)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(blockAction.getPos()).
						withEvent(new ExecuteBlockEventEvent(eventType, blockAction, returnValue, failInfo))
		);
	}

	public static void onScheduleBlockEvent(World world, BlockEvent blockAction, boolean success)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(blockAction.getPos()).
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
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new EmitBlockUpdateEvent(eventType, block, methodName))
		);
	}

	public static void onEmitBlockUpdateRedstoneDust(World world, Block block, BlockPos pos, EventType eventType, String methodName, Collection<BlockPos> updateOrder)
	{
		onEvent(
				MicroTimingContext.create().
						withWorld(world).withBlockPos(pos).
						withEvent(new EmitBlockUpdateRedstoneDustEvent(eventType, block, methodName, pos, updateOrder))
		);
	}

	/*
	 * ------------
	 *  Tick Stage
	 * ------------
	 */

	public static void setTickStage(World world, TickStage stage)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStage(stage));
	}

	public static void setTickStage(TickStage stage)
	{
		if (instance != null)
		{
			for (MicroTimingLogger logger : instance.loggers.values())
			{
				logger.setTickStage(stage);
			}
		}
	}

	public static void setTickStageDetail(World world, String detail)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStageDetail(detail));
	}

	public static void setTickStageExtra(World world, TickStageExtraBase stage)
	{
		getWorldLogger(world).ifPresent(logger -> logger.setTickStageExtra(stage));
	}

	public static void setTickStageExtra(TickStageExtraBase stage)
	{
		if (instance != null)
		{
			for (MicroTimingLogger logger : instance.loggers.values())
			{
				logger.setTickStageExtra(stage);
			}
		}
	}

	private void flush(long gameTime) // needs to call at the end of a gt
	{
		if (gameTime != this.lastFlushTime)
		{
			this.lastFlushTime = gameTime;
			for (MicroTimingLogger logger : this.loggers.values())
			{
				logger.flushMessages();
			}
		}
	}

	public static void flushMessages(long gameTime) // needs to call at the end of a gt
	{
		if (instance != null && isLoggerActivated())
		{
			instance.flush(gameTime);
		}
	}

	/*
	 * ----------------
	 *   Marker Logic
	 * ----------------
	 */

	public static boolean onPlayerRightClick(PlayerEntity playerEntity, Hand hand, BlockPos blockPos)
	{
		if (MicroTimingUtil.isMarkerEnabled() && playerEntity instanceof ServerPlayerEntity && hand == Hand.MAIN_HAND)
		{
			ItemStack itemStack = playerEntity.getMainHandStack();
			Item holdingItem = itemStack.getItem();
			if (holdingItem instanceof DyeItem && MicroTimingUtil.isPlayerSubscribed(playerEntity))
			{
				BaseText name = null;
				if (itemStack.hasCustomName())
				{
					name = (BaseText)itemStack.getName();
				}
				// server-side check will be in addMarker
				MicroTimingMarkerManager.getInstance().addMarker(playerEntity, blockPos, ((DyeItem)holdingItem).getColor(), name);
				return true;
			}
		}
		return false;
	}
}
