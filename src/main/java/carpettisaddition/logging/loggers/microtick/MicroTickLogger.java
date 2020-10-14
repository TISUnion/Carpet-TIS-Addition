package carpettisaddition.logging.loggers.microtick;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.TranslatableLogger;
import carpettisaddition.logging.loggers.microtick.events.*;
import carpettisaddition.logging.loggers.microtick.message.ArrangedMessage;
import carpettisaddition.logging.loggers.microtick.message.MessageList;
import carpettisaddition.logging.loggers.microtick.message.MicroTickMessage;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.block.*;
import net.minecraft.server.world.BlockAction;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MicroTickLogger extends TranslatableLogger
{
	// [stage][detail]^[extra]

	private static final Direction[] DIRECTION_VALUES = Direction.values();
	private String stage;
	private String stageDetail;
	private ToTextAble stageExtra;
	private final World world;
	public final MessageList messages = new MessageList();
	private final LongOpenHashSet pistonBlockEventSuccessPosition = new LongOpenHashSet();
	private final BaseText dimensionDisplayTextGray;

	public MicroTickLogger(World world)
	{
		super("microtick");
		this.world = world;
		if (world != null)
		{
			this.dimensionDisplayTextGray = Util.getDimensionNameText(this.world.getDimension().getType());
			this.dimensionDisplayTextGray.getStyle().setColor(Formatting.GRAY);
		}
		else
		{
			this.dimensionDisplayTextGray = null;
		}
	}
	
	public void setTickStage(String stage)
	{
		this.stage = stage;
	}
	public String getTickStage()
	{
		return this.stage;
	}
	public void setTickStageDetail(String stage)
	{
		this.stageDetail = stage;
	}
	public String getTickStageDetail()
	{
		return this.stageDetail;
	}
	public void setTickStageExtra(ToTextAble extra)
	{
		this.stageExtra = extra;
	}
	public ToTextAble getTickStageExtra()
	{
		return this.stageExtra;
	}

	/*
	 * --------------
	 *  Block Update
	 * --------------
	 */

	public void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, String updateTypeExtra, EventType eventType)
	{
		for (Direction facing: DIRECTION_VALUES)
		{
			BlockPos blockEndRodPos = pos.offset(facing);
			BlockState iBlockState = world.getBlockState(blockEndRodPos);
			if (iBlockState.getBlock() == Blocks.END_ROD && iBlockState.get(FacingBlock.FACING).getOpposite() == facing)
			{
				DyeColor color = MicroTickUtil.getWoolColor(world, blockEndRodPos);
				if (color != null)
				{
					this.addMessage(color, pos, world, new BlockUpdateEmitEvent(eventType, fromBlock, updateType, updateTypeExtra));
					break;
				}
			}
		}
	}

	private final static List<Property<?>> INTEREST_PROPERTIES = Lists.newArrayList(Properties.POWERED, Properties.LIT);

	public void onSetBlockState(World world, BlockPos pos, BlockState newState, Boolean returnValue, EventType eventType)
	{
		// lazy loading
		BlockState oldState = null;
		DyeColor color = null;
		BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, returnValue, newState.getBlock());

		if (eventType != EventType.ACTION_END)
		{
			for (Property<?> property: INTEREST_PROPERTIES)
			{
				Optional<?> newValue = MicroTickUtil.getBlockStateProperty(newState, property);
				if (newValue.isPresent())
				{
					if (oldState == null)
					{
						oldState = world.getBlockState(pos);
						if (oldState.getBlock() != newState.getBlock())
						{
							break;
						}
						color = MicroTickUtil.getWoolColor(world, pos);
						if (color == null)
						{
							break;
						}
					}
					Optional<?> oldValue = MicroTickUtil.getBlockStateProperty(oldState, property);
					oldValue.ifPresent(ov -> event.addChanges(property.getName(), ov, newValue.get()));
				}
			}
			if (event.hasChanges())
			{
				this.addMessage(color, pos, world, event);
			}
		}
		else
		{
			color = MicroTickUtil.getWoolColor(world, pos);
			if (color != null)
			{
				this.addMessage(color, pos, world, event);
			}
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public void onExecuteTileTick(World world, ScheduledTick<Block> event, EventType eventType)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, event.pos);
		if (color != null)
		{
			this.addMessage(color, event.pos, world, new ExecuteTileTickEvent(eventType, event));
		}
	}

	public void onScheduleTileTick(World world, Block block, BlockPos pos, int delay, TickPriority priority)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, new ScheduleTileTickEvent(block, pos, delay, priority));
		}
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	public void onExecuteBlockEvent(World world, BlockAction blockAction, Boolean returnValue, EventType eventType)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, blockAction.getPos());
		if (color != null)
		{
			if (blockAction.getBlock() instanceof PistonBlock)
			{
				// ignore failure after a success block event of piston in the same gt
				if (pistonBlockEventSuccessPosition.contains(blockAction.getPos().asLong()))
				{
					return;
				}
				if (returnValue != null && returnValue)
				{
					this.pistonBlockEventSuccessPosition.add(blockAction.getPos().asLong());
				}
			}
			this.addMessage(color, blockAction.getPos(), world, new ExecuteBlockEventEvent(eventType, blockAction, returnValue));
		}
	}

	public void onScheduleBlockEvent(World world, BlockAction blockAction)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, blockAction.getPos());
		if (color != null)
		{
			this.addMessage(color, blockAction.getPos(), world, new ScheduleBlockEventEvent(blockAction));
		}
	}

	/*
	 * ------------------
	 *  Component things
	 * ------------------
	 */

	public void onEmitBlockUpdate(World world, Block block, BlockPos pos, EventType eventType, String methodName)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, new EmitBlockUpdateEvent(eventType, block, methodName));
		}
	}

	public void addMessage(DyeColor color, BlockPos pos, World world, BaseEvent event)
	{
		MicroTickMessage message = new MicroTickMessage(this, world.getDimension().getType(), pos, color, event);
		if (message.event.getEventType() != EventType.ACTION_END)
		{
			this.messages.addMessageAndIndent(message);
		}
		else
		{
			this.messages.addMessageAndUnIndent(message);
		}
	}

	private BaseText[] getMessages(boolean uniqueOnly)
	{
		List<BaseText> msg = Lists.newArrayList();
		Set<MicroTickMessage> messageHashSet = Sets.newHashSet();
		msg.add(Messenger.s(" "));
		msg.add(Messenger.c(
				"f [GameTime ",
				"g " + this.world.getTime(),
				"f  @ ",
				this.dimensionDisplayTextGray,
				"f ] ------------"
		));
		for (ArrangedMessage message : this.messages.toList())
		{
			boolean showThisMessage = !uniqueOnly || messageHashSet.add(message.getMessage());
			if (showThisMessage)
			{
				msg.add(message.toText());
			}
		}
		return msg.toArray(new BaseText[0]);
	}

	public void flushMessages()
	{
		if (!this.messages.isEmpty())
		{
			Map<Boolean, BaseText[]> flushedMessages = new Reference2ObjectArrayMap<>();
			flushedMessages.put(false, getMessages(false));
			flushedMessages.put(true, getMessages(true));
			LoggerRegistry.getLogger("microtick").log((option) ->
			{
				boolean uniqueOnly = option.equals("unique");
				return flushedMessages.get(uniqueOnly);
			});
		}
		this.messages.clear();
		this.pistonBlockEventSuccessPosition.clear();
	}
}
