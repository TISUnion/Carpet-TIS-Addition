package carpettisaddition.logging.loggers.microtick;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.TranslatableLogger;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.MessageType;
import carpettisaddition.logging.loggers.microtick.types.PistonBlockEventType;
import carpettisaddition.logging.loggers.microtick.types.PowerState;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.*;
import net.minecraft.server.world.BlockAction;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.max;

public class MicroTickLogger extends TranslatableLogger
{
	// [stage][detail]^[extra]

	private static final Direction[] DIRECTION_VALUES = Direction.values();
	private String stage;
	private String stageDetail;
	private TickStage stageExtra;
	private int indentation;
	private final World world;
	public final List<MicroTickMessage> messages = Lists.newLinkedList();
	private final LongOpenHashSet pistonBlockEventSuccessPosition = new LongOpenHashSet();
	private final Text dimensionDisplayTextGray;

	public MicroTickLogger(World world)
	{
		super("microtick");
		this.world = world;
		this.indentation = 0;
		this.dimensionDisplayTextGray = Util.getDimensionNameText(this.world.getDimension().getType()).deepCopy();
		this.dimensionDisplayTextGray.getStyle().setColor(Formatting.GRAY);
	}

	public void doIndent(MessageType messageType)
	{
		if (messageType == MessageType.ACTION_START)
		{
			this.indentation++;
		}
	}
	public void unIndent(MessageType messageType)
	{
		if (messageType == MessageType.ACTION_END)
		{
			this.indentation = max(this.indentation - 1, 0);
		}
	}
	public int getIndent()
	{
		return this.indentation;
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
	public void setTickStageExtra(TickStage extra)
	{
		this.stageExtra = extra;
	}
	public TickStage getTickStageExtra()
	{
		return this.stageExtra;
	}

	public void onBlockUpdate(World world, BlockPos pos, Block fromBlock, BlockUpdateType updateType, String updateTypeExtra, MessageType messageType)
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
					this.addMessage(color, pos, world, messageType, new Object[]{
							MicroTickUtil.getTranslatedName(fromBlock),
							String.format("q  %s", messageType),
							String.format("c  %s", updateType),
							String.format("^w %s", updateTypeExtra)
					});
					break;
				}
			}
		}
	}

	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	public void onExecuteTileTickEvent(World world, ScheduledTick<Block> event, MessageType messageType)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, event.pos);
		if (color != null)
		{
			List<Object> list = Lists.newLinkedList();
			list.add(MicroTickUtil.getTranslatedName(event.getObject()));
			list.add("q  Execute");
			list.add("c  TileTick");
			if (messageType == MessageType.ACTION_END)
			{
				list.add(String.format("q  %s", messageType));
			}
			list.add(String.format("^w Priority: %d (%s)", event.priority.getIndex(), event.priority));
			this.addMessage(color, event.pos, world, messageType, list.toArray(new Object[0]));
		}
	}

	public void onScheduleTileTickEvent(World world, Block block, BlockPos pos, int delay, TickPriority priority)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, MessageType.EVENT, new Object[]{
					MicroTickUtil.getTranslatedName(block),
					"q  Scheduled",
					"c  TileTick",
					String.format("^w Delay: %dgt\nPriority: %d (%s)", delay, priority.getIndex(), priority)
			});
		}
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	public void onExecuteBlockEvent(World world, BlockAction blockAction, Boolean returnValue, MessageType messageType)
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
			List<Object> list = Lists.newLinkedList();
			list.add(MicroTickUtil.getTranslatedName(blockAction.getBlock()));
			list.add("q  Executed");
			list.add(String.format("c  %s", PistonBlockEventType.getById(blockAction.getType())));
			list.add(MicroTickUtil.getBlockEventMessageExtra(blockAction));
			if (returnValue != null)
			{
				list.add(String.format("%s  %s", MicroTickUtil.getBooleanColor(returnValue), returnValue ? "Succeed" : "Failed"));
			}
			this.addMessage(color, blockAction.getPos(), world, messageType, list.toArray(new Object[0]));
		}
	}

	public void onScheduleBlockEvent(World world, BlockAction blockAction)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, blockAction.getPos());
		if (color != null)
		{
			this.addMessage(color, blockAction.getPos(), world, MessageType.EVENT, new Object[]{
					MicroTickUtil.getTranslatedName(blockAction.getBlock()),
					"q  Scheduled",
					"c  BlockEvent",
					MicroTickUtil.getBlockEventMessageExtra(blockAction)
			});
		}
	}

	public void onComponentPowered(World world, BlockPos pos, PowerState poweredState)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, MessageType.EVENT, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					String.format("c  %s", poweredState)
			});
		}
	}

	public void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, MessageType.EVENT, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					String.format("c  %s", litState ? "Lit" : "Unlit")
			});
		}
	}

	// #(color, pos) texts[] at stage(detail, extra, dimension)
	public void addMessage(DyeColor color, BlockPos pos, World world, MessageType messageType, Object [] texts)
	{
		this.unIndent(messageType);
		MicroTickMessage message = new MicroTickMessage(this, world.getDimension().getType(), pos, color, messageType, texts);
		this.messages.add(message);
		this.doIndent(messageType);
	}

	void flushMessages()
	{
		if (this.messages.isEmpty())
		{
			return;
		}
		LoggerRegistry.getLogger("microtick").log( (option) ->
		{
			boolean uniqueOnly = option.equals("unique");
			List<BaseText> msg = Lists.newLinkedList();
			Set<MicroTickMessage> messageHashSet = Sets.newHashSet();
			Iterator<MicroTickMessage> iterator = this.messages.iterator();
			msg.add(Messenger.s(" "));
			msg.add(Messenger.c(
					"f [GameTime ",
					"g " + this.world.getTime(),
					"f  @ ",
					this.dimensionDisplayTextGray,
					"f ] ------------"
			));
			while (iterator.hasNext())
			{
				MicroTickMessage message = iterator.next();

				boolean flag = !uniqueOnly;
				if (!messageHashSet.contains(message))
				{
					messageHashSet.add(message);
					flag = true;
				}
				if (flag)
				{
					msg.add(message.toText());
				}
			}
			return msg.toArray(new BaseText[0]);
		});
		this.messages.clear();
		this.pistonBlockEventSuccessPosition.clear();
	}
}
