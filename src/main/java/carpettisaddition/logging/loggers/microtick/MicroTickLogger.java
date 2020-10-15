package carpettisaddition.logging.loggers.microtick;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.TranslatableLogger;
import carpettisaddition.logging.loggers.microtick.events.*;
import carpettisaddition.logging.loggers.microtick.message.IndentedMessage;
import carpettisaddition.logging.loggers.microtick.message.MessageList;
import carpettisaddition.logging.loggers.microtick.message.MicroTickMessage;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.server.world.BlockAction;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class MicroTickLogger extends TranslatableLogger
{
	// [stage][detail]^[extra]

	private String stage;
	private String stageDetail;
	private TickStageExtraBase stageExtra;
	private final World world;
	public final MessageList messageList = new MessageList();
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
	public void setTickStageDetail(String stageDetail)
	{
		this.stageDetail = stageDetail;
	}
	public String getTickStageDetail()
	{
		return this.stageDetail;
	}
	public void setTickStageExtra(TickStageExtraBase extra)
	{
		this.stageExtra = extra;
	}
	public TickStageExtraBase getTickStageExtra()
	{
		return this.stageExtra;
	}

	/*
	 * --------------
	 *  Block Update
	 * --------------
	 */

	public void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, Supplier<String> updateTypeExtra, EventType eventType)
	{
		Optional<DyeColor> color = MicroTickUtil.getEndRodWoolColor(world, pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, pos, world, new DetectBlockUpdateEvent(eventType, fromBlock, updateType, updateTypeExtra.get())));
	}

	private final static Set<Property<?>> INTEREST_PROPERTIES = new ReferenceArraySet<>();
	static
	{
		INTEREST_PROPERTIES.add(Properties.POWERED);
		INTEREST_PROPERTIES.add(Properties.LIT);
	}

	public void onSetBlockState(World world, BlockPos pos, BlockState oldState, BlockState newState, Boolean returnValue, EventType eventType)
	{
		// lazy loading
		DyeColor color = null;
		BlockStateChangeEvent event = new BlockStateChangeEvent(eventType, returnValue, newState.getBlock());

		for (Property<?> property: newState.getProperties())
		{
			if (INTEREST_PROPERTIES.contains(property))
			{
				if (color == null)
				{
					Optional<DyeColor> optionalDyeColor = MicroTickUtil.getWoolOrEndRodWoolColor(world, pos);
					if (!optionalDyeColor.isPresent())
					{
						break;
					}
					color = optionalDyeColor.get();
				}
				event.addChanges(property.getName(), newState.get(property), oldState.get(property));
			}
		}
		if (event.hasChanges())
		{
			this.addMessage(color, pos, world, event);
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public void onExecuteTileTick(World world, ScheduledTick<Block> event, EventType eventType)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, event.pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, event.pos, world, new ExecuteTileTickEvent(eventType, event)));
	}

	public void onScheduleTileTick(World world, Block block, BlockPos pos, int delay, TickPriority priority, Boolean success)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, pos, world, new ScheduleTileTickEvent(block, pos, delay, priority, success)));
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	public void onExecuteBlockEvent(World world, BlockAction blockAction, Boolean returnValue, EventType eventType)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, blockAction.getPos());
		if (color.isPresent())
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
			this.addMessage(color.get(), blockAction.getPos(), world, new ExecuteBlockEventEvent(eventType, blockAction, returnValue));
		}
	}

	public void onScheduleBlockEvent(World world, BlockAction blockAction)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, blockAction.getPos());
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, blockAction.getPos(), world, new ScheduleBlockEventEvent(blockAction)));
	}

	/*
	 * ------------------
	 *  Component things
	 * ------------------
	 */

	public void onEmitBlockUpdate(World world, Block block, BlockPos pos, EventType eventType, String methodName)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, pos, world, new EmitBlockUpdateEvent(eventType, block, methodName)));
	}

	/*
	 * -----------------------
	 *  Component things ends
	 * -----------------------
	 */

	public void addMessage(DyeColor color, BlockPos pos, World world, BaseEvent event)
	{
		MicroTickMessage message = new MicroTickMessage(this, world.getDimension().getType(), pos, color, event);
		if (message.getEvent().getEventType() != EventType.ACTION_END)
		{
			this.messageList.addMessageAndIndent(message);
		}
		else
		{
			this.messageList.addMessageAndUnIndent(message);
		}
	}

	private BaseText[] getTrimmedMessages(List<IndentedMessage> flushedMessages, boolean uniqueOnly)
	{
		List<BaseText> msg = Lists.newArrayList();
		Set<MicroTickMessage> messageHashSet = Sets.newHashSet();
		msg.add(Messenger.s(" "));
		msg.add(Messenger.c(
				String.format("f [%s ", this.tr("GameTime")),
				"g " + this.world.getTime(),
				"f  @ ",
				this.dimensionDisplayTextGray,
				"f ] ------------"
		));
		for (IndentedMessage message : flushedMessages)
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
		if (!this.messageList.isEmpty())
		{
			List<IndentedMessage> flushedMessages = this.messageList.flush();
			Map<Boolean, BaseText[]> flushedTrimmedMessages = new Reference2ObjectArrayMap<>();
			flushedTrimmedMessages.put(false, getTrimmedMessages(flushedMessages, false));
			flushedTrimmedMessages.put(true, getTrimmedMessages(flushedMessages, true));
			LoggerRegistry.getLogger("microtick").log((option) ->
			{
				boolean uniqueOnly = option.equals("unique");
				return flushedTrimmedMessages.get(uniqueOnly);
			});
		}
		this.pistonBlockEventSuccessPosition.clear();
	}
}
