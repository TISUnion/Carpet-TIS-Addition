package carpettisaddition.commands.manipulate.container;

import carpettisaddition.mixins.command.manipulate.WorldAccessor;
import carpettisaddition.utils.IdentifierUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.minecraft.command.argument.BlockPosArgumentType.blockPos;
import static net.minecraft.command.argument.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TileEntityListController extends AbstractEntityListController
{
	private static final int TOP_N = 10;

	public TileEntityListController()
	{
		super("tile_entity");
	}

	@Override
	protected boolean canManipulate(ServerWorld world)
	{
		return !((WorldAccessor)world).isIteratingTickingBlockEntities();
	}

	@Override
	protected int processWholeList(ServerWorld world, Consumer<List<?>> collectionOperator)
	{
		List<BlockEntityTickInvoker> blockEntityTickers = ((WorldAccessor)world).getBlockEntityTickers();
		collectionOperator.accept(blockEntityTickers);
		return blockEntityTickers.size();
	}

	private int queryTileEntityInfo(ServerCommandSource source, BlockPos pos)
	{
		ServerWorld world = source.getWorld();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity != null)
		{
			int index = world.tickingBlockEntities.indexOf(blockEntity);
			Messenger.tell(source, Arrays.asList(
					tr("query.title", Messenger.coord(pos, DimensionWrapper.of(world))),
					Messenger.format("- %1$s: %2$s", tr("type"), Messenger.blockEntity(blockEntity)),
					Messenger.format("- %1$s: %2$s", tr("ticking_order"), index != -1 ? index : "N/A")
			));
			return 1;
		}
		else
		{
			Messenger.tell(source, tr("not_found", Messenger.coord(pos, DimensionWrapper.of(world))));
			return 0;
		}
	}

	private void showTopNInCollection(ServerCommandSource source, BaseText name, Collection<BlockEntity> blockEntities)
	{
		Multiset<BlockEntityType<?>> counter = HashMultiset.create();
		blockEntities.stream().map(BlockEntity::getType).forEach(counter::add);
		List<Multiset.Entry<BlockEntityType<?>>> topN = counter.entrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingInt(Multiset.Entry::getCount))).
				limit(TOP_N).
				collect(Collectors.toList());

		Messenger.tell(source, Messenger.format("%1$s: %2$sx", name, blockEntities.size()));
		for (int i = 0; i < topN.size(); i++)
		{
			Multiset.Entry<BlockEntityType<?>> entry = topN.get(i);
			Messenger.tell(source, Messenger.formatting(Messenger.format(
					"%1$s. %2$s: %3$sx",
					i + 1,
					IdentifierUtil.id(entry.getElement()),
					entry.getCount()
			), "g"));
		}
	}

	private int showStatistic(ServerCommandSource source)
	{
		ServerWorld world = source.getWorld();
		Messenger.tell(source, Messenger.format(
				"===== %1$s =====",
				tr("statistic.title", Messenger.dimension(DimensionWrapper.of(world)))
		));
		this.showTopNInCollection(source, tr("statistic.all"), world.blockEntities);
		this.showTopNInCollection(source, tr("statistic.ticking"), world.tickingBlockEntities);
		return 1;
	}

	@Override
	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode()
	{
		return super.getCommandNode().
				then(literal("statistic").
						executes(c -> this.showStatistic(c.getSource()))
				).
				then(literal("query").
						then(argument("pos", blockPos()).
								executes(c -> this.queryTileEntityInfo(c.getSource(), getLoadedBlockPos(c, "pos")))
						)
				);
	}
}
