package carpettisaddition.commands.manipulate.container;

import carpettisaddition.logging.loggers.microtiming.events.ExecuteBlockEventEvent;
import carpettisaddition.mixins.command.manipulate.ServerWorldAccessor;
import carpettisaddition.utils.DimensionWrapper;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.Block;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.util.math.BlockPos;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.BlockPosArgumentType.getBlockPos;
import static net.minecraft.command.argument.BlockStateArgumentType.blockState;
import static net.minecraft.command.argument.BlockStateArgumentType.getBlockState;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BlockEventQueueController extends AbstractContainerController
{
	public BlockEventQueueController()
	{
		super("block_event");
	}

	public int removeAt(ServerCommandSource source, BlockPos blockPos)
	{
		int counter = 0;
		ObjectLinkedOpenHashSet<BlockEvent> queue = ((ServerWorldAccessor)source.getWorld()).getSyncedBlockEventQueue();
		for (ObjectListIterator<BlockEvent> iterator = queue.iterator(); iterator.hasNext(); )
		{
			BlockEvent be = iterator.next();
			if (be.pos().equals(blockPos))
			{
				iterator.remove();
				counter++;
			}
		}
		Messenger.tell(source, Messenger.tr("removed", counter));
		return counter;
	}

	public int addEvent(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		BlockPos blockPos = getBlockPos(context, "pos");
		Block block = getBlockState(context, "block").getBlockState().getBlock();
		int type = getInteger(context, "type");
		int data = getInteger(context, "data");
		BlockEvent blockAction = new BlockEvent(blockPos, block, type, data);

		Messenger.tell(source, tr(
				"scheduled",
				Messenger.fancy(tr("item_name"), ExecuteBlockEventEvent.getMessageExtraMessengerHoverText(blockAction), null),
				Messenger.coord(blockPos, DimensionWrapper.of(source.getWorld()))
		));
		source.getWorld().addSyncedBlockEvent(blockPos, block, type, data);
		return 1;
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode()
	{
		return super.getCommandNode().
				then(literal("remove").
						then(argument("pos", blockPos()).
								executes(c -> this.removeAt(c.getSource(), getBlockPos(c, "pos")))
						)
				).
				then(literal("add").
						then(argument("pos", blockPos()).
								then(argument("block", blockState()).
										then(argument("type", integer()).
												then(argument("data", integer()).
														executes(this::addEvent)
												)
										)
								)
						)
				);
	}
}
