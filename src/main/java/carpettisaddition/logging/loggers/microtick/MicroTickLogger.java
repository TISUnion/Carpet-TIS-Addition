package carpettisaddition.logging.loggers.microtick;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.TranslatableLogger;
import carpettisaddition.logging.loggers.microtick.enums.ActionRelation;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.enums.PistonBlockEventType;
import carpettisaddition.logging.loggers.microtick.tickstages.TickStage;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MicroTickLogger extends TranslatableLogger
{
	private static final Direction[] DIRECTION_VALUES = Direction.values();
	private String stage;
	private String stageDetail;
	private TickStage stageExtra;
	private final World world;
	public final List<MicroTickMessage> messages = Lists.newLinkedList();
	private final LongOpenHashSet pistonBlockEventSuccessPosition = new LongOpenHashSet();
	private final Text dimensionDisplayTextGray;

	public MicroTickLogger(World world)
	{
		super("microtick");
		this.world = world;
		this.dimensionDisplayTextGray = Util.getDimensionNameText(this.world.getDimension().getType()).deepCopy();
		this.dimensionDisplayTextGray.getStyle().setColor(Formatting.GRAY);
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

	public void onBlockUpdate(World world, BlockPos pos, Block fromBlock, ActionRelation actionType, BlockUpdateType updateType, String updateTypeExtra)
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
					this.addMessage(color, pos, world, new Object[]{
							MicroTickUtil.getTranslatedName(fromBlock),
							String.format("q  %s", actionType),
							String.format("c  %s", updateType),
							String.format("^w %s", updateTypeExtra)
					});
				}
			}
		}
	}
	public void onComponentAddToTileTickList(World world, BlockPos pos, int delay, TickPriority priority)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			System.err.println(world.getTime() + " " + delay);
			this.addMessage(color, pos, world, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					"q  Scheduled",
					"c  TileTick",
					String.format("^w Delay: %dgt\nPriority: %d (%s)", delay, priority.getIndex(), priority)
			});
		}
	}

	public void onPistonAddBlockEvent(World world, BlockPos pos, int eventID, int eventParam)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					"q  Scheduled",
					"c  BlockEvent",
					MicroTickUtil.getBlockEventMessageExtra(eventID, eventParam)
			});
		}
	}

	// "block" only overwrites displayed name
	public void onPistonExecuteBlockEvent(World world, BlockPos pos, Block block, int eventID, int eventParam, boolean success)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			if (success)
			{
				this.pistonBlockEventSuccessPosition.add(pos.asLong());
			}
			else if (pistonBlockEventSuccessPosition.contains(pos.asLong())) // ignore failure after a success blockevent of piston in the same gt
			{
				return;
			}
			addMessage(color, pos, world, new Object[]{
					MicroTickUtil.getTranslatedName(block),
					"q  Executed",
					String.format("c  %s", PistonBlockEventType.getById(eventID)),
					MicroTickUtil.getBlockEventMessageExtra(eventID, eventParam),
					String.format("%s  %s", MicroTickUtil.getBooleanColor(success), success ? "Succeed" : "Failed")
			});
		}
	}

	public void onComponentPowered(World world, BlockPos pos, boolean poweredState)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					String.format("c  %s", poweredState ? "Powered" : "Depowered")
			});
		}
	}

	public void onRedstoneTorchLit(World world, BlockPos pos, boolean litState)
	{
		DyeColor color = MicroTickUtil.getWoolColor(world, pos);
		if (color != null)
		{
			this.addMessage(color, pos, world, new Object[]{
					MicroTickUtil.getTranslatedName(world.getBlockState(pos).getBlock()),
					String.format("c  %s", litState ? "Lit" : "Unlit")
			});
		}
	}

	// #(color, pos) texts[] at stage(detail, extra, dimension)
	public void addMessage(DyeColor color, BlockPos pos, DimensionType dim, Object [] texts)
	{
		MicroTickMessage message = new MicroTickMessage(this, dim, pos, color, texts);
		this.messages.add(message);
	}
	public void addMessage(DyeColor color, BlockPos pos, World world, Object [] texts)
	{
		this.addMessage(color, pos, world.getDimension().getType(), texts);
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
					List<Object> line = Lists.newLinkedList();
					line.add(message.getHashTag());
					for (Object text: message.texts)
					{
						if (text instanceof Text || text instanceof String)
						{
							line.add(text);
						}
					}
					line.add("w  ");
					line.add(message.getStage());
					line.add("w  ");
					line.add(message.getStackTrace());
					msg.add(Messenger.c(line.toArray(new Object[0])));
				}
			}
			return msg.toArray(new BaseText[0]);
		});
		this.messages.clear();
		this.pistonBlockEventSuccessPosition.clear();
	}
}
