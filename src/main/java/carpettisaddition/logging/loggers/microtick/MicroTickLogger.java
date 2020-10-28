package carpettisaddition.logging.loggers.microtick;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.BaseLogger;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import carpettisaddition.logging.loggers.microtick.enums.TickStage;
import carpettisaddition.logging.loggers.microtick.events.BaseEvent;
import carpettisaddition.logging.loggers.microtick.events.BlockStateChangeEvent;
import carpettisaddition.logging.loggers.microtick.message.IndentedMessage;
import carpettisaddition.logging.loggers.microtick.message.MessageList;
import carpettisaddition.logging.loggers.microtick.message.MessageType;
import carpettisaddition.logging.loggers.microtick.message.MicroTickMessage;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStageExtraBase;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class MicroTickLogger extends BaseLogger
{
	// [stage][detail]^[extra]

	private TickStage stage;
	private String stageDetail;
	private TickStageExtraBase stageExtra;
	private final ServerWorld world;
	public final MessageList messageList = new MessageList();
	private final BaseText dimensionDisplayTextGray;

	public MicroTickLogger(ServerWorld world)
	{
		super("microtick");
		this.world = world;
		if (world != null)
		{
			this.dimensionDisplayTextGray = Util.getFancyText(
					"g",
					Util.getDimensionNameText(this.world.getRegistryKey()),
					Messenger.s(this.world.getRegistryKey().getValue().toString()),
					null
			);
		}
		else
		{
			this.dimensionDisplayTextGray = null;
		}
	}
	
	public void setTickStage(TickStage stage)
	{
		this.stage = stage;
		this.stageDetail = null;
		this.stageExtra = null;
	}

	public TickStage getTickStage()
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

	public ServerWorld getWorld()
	{
		return this.world;
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
				event.addChanges(property.getName(), oldState.get(property), newState.get(property));
			}
		}
		if (event.hasChanges())
		{
			this.addMessage(color, world, pos, event);
		}
	}

	public void addMessage(DyeColor color, World world, BlockPos pos, BaseEvent event)
	{
		MicroTickMessage message = new MicroTickMessage(this, world.getRegistryKey(), pos, color, event);
		if (message.getEvent().getEventType() != EventType.ACTION_END)
		{
			this.messageList.addMessageAndIndent(message);
		}
		else
		{
			this.messageList.addMessageAndUnIndent(message);
		}
	}

	public void addMessage(World world, BlockPos pos, BaseEvent event)
	{
		Optional<DyeColor> color = MicroTickUtil.getWoolOrEndRodWoolColor(world, pos);
		color.ifPresent(dyeColor -> this.addMessage(dyeColor, world, pos, event));
	}

	private BaseText[] getTrimmedMessages(List<IndentedMessage> flushedMessages, boolean uniqueOnly)
	{
		List<BaseText> msg = Lists.newArrayList();
		Set<MicroTickMessage> messageHashSet = Sets.newHashSet();
		msg.add(Messenger.s(" "));
		msg.add(Messenger.c(
				"f [",
				"f " + this.tr("GameTime"),
				"^w world.getTime()",
				"g  " + this.world.getTime(),
				"f  @ ",
				this.dimensionDisplayTextGray,
				"f ] ------------"
		));
		for (IndentedMessage message : flushedMessages)
		{
			boolean showThisMessage = !uniqueOnly || messageHashSet.add(message.getMessage()) || message.getMessage().getMessageType() == MessageType.PROCEDURE;
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
			if (!flushedMessages.isEmpty())
			{
				Map<Boolean, BaseText[]> flushedTrimmedMessages = new Reference2ObjectArrayMap<>();
				flushedTrimmedMessages.put(false, getTrimmedMessages(flushedMessages, false));
				flushedTrimmedMessages.put(true, getTrimmedMessages(flushedMessages, true));
				LoggerRegistry.getLogger("microtick").log((option) ->
				{
					boolean uniqueOnly = option.equals("unique");
					return flushedTrimmedMessages.get(uniqueOnly);
				});
			}
		}
	}
}
