package carpettisaddition.commands.manipulate.container;

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerTickScheduler;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.argument.BlockStateArgumentType.blockState;
import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TileTickQueueController extends AbstractContainerController
{
	public TileTickQueueController()
	{
		super("tile_tick");
	}

	private int remove(ServerTickScheduler<?> serverTickScheduler, BlockPos blockPos)
	{
		BlockBox blockBox = new BlockBox(blockPos, blockPos.add(1, 1, 1));
		List<?> removed = serverTickScheduler.getScheduledTicks(blockBox, true, false);
		return removed.size();
	}

	private int removeAt(ServerCommandSource source, BlockPos blockPos)
	{
		int counter = 0;
		counter += this.remove(source.getWorld().getBlockTickScheduler(), blockPos);
		counter += this.remove(source.getWorld().getFluidTickScheduler(), blockPos);
		Messenger.tell(source, tr("removed", counter));
		return counter;
	}

	private int addTileTickEvent(CommandContext<ServerCommandSource> context, @Nullable Object priorityArg) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		BlockPos blockPos = getLoadedBlockPos(context, "pos");
		Block block = getBlockState(context, "block").getBlockState().getBlock();
		int delay = getInteger(context, "delay");
		TickPriority priority = TickPriority.NORMAL;
		if (priorityArg instanceof TickPriority)
		{
			priority = (TickPriority)priorityArg;
		}
		if (priorityArg instanceof Integer)
		{
			priority = TickPriority.byIndex((Integer)priorityArg);
		}

		source.getWorld().getBlockTickScheduler().schedule(blockPos, block, delay, priority);
		Messenger.tell(source, tr(
				"scheduled",
				Messenger.fancy(tr("item_name"), tr("item_description", Messenger.block(block), delay, priority.getIndex(), priority), null),
				Messenger.coord(blockPos, DimensionWrapper.of(source.getWorld()))
		));
		return 1;
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode()
	{
		return super.getCommandNode().
				then(literal("remove").
						then(argument("pos", blockPos()).
								executes(c -> this.removeAt(c.getSource(), getLoadedBlockPos(c, "pos")))
						)
				).
				then(literal("add").
						then(argument("pos", blockPos()).
								then(argument("block", blockState()).
										then(argument("delay", integer()).
												executes(c -> this.addTileTickEvent(c, null)).
												then(argument("priority", integer(TickPriority.EXTREMELY_HIGH.getIndex(), TickPriority.EXTREMELY_LOW.getIndex())).
														executes(c -> this.addTileTickEvent(c, getInteger(c, "priority")))
												)
										)
								)
						)
				);
	}
}
