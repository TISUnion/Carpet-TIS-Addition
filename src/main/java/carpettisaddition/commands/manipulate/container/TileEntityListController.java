package carpettisaddition.commands.manipulate.container;

import carpettisaddition.mixins.command.manipulate.WorldAccessor;
import carpettisaddition.utils.IdentifierUtil;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.command.arguments.BlockPosArgumentType.getLoadedBlockPos;
import static net.minecraft.command.arguments.IdentifierArgumentType.getIdentifier;
import static net.minecraft.command.arguments.IdentifierArgumentType.identifier;
import static net.minecraft.command.arguments.NbtCompoundTagArgumentType.getCompoundTag;
import static net.minecraft.command.arguments.NbtCompoundTagArgumentType.nbtCompound;
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
		collectionOperator.accept(world.tickingBlockEntities);
		return world.tickingBlockEntities.size();
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

	private int removeAt(ServerCommandSource source, BlockPos pos)
	{
		ServerWorld world = source.getWorld();
		world.removeBlockEntity(pos);
		Messenger.tell(source, tr("remove", Messenger.coord(pos, DimensionWrapper.of(world))));
		return 1;
	}

	private int createAt(CommandContext<ServerCommandSource> context) throws CommandSyntaxException
	{
		ServerCommandSource source = context.getSource();
		BlockPos blockPos = getLoadedBlockPos(context, "pos");
		Identifier id = getIdentifier(context, "block");

		Block block = Registry.BLOCK.get(id);
		if (!(block instanceof BlockEntityProvider))
		{
			Messenger.tell(source, tr("create.not_provider", id));
			return 0;
		}

		CompoundTag nbt = null;
		try
		{
			nbt = getCompoundTag(context, "data");
		}
		catch (IllegalArgumentException ignored)
		{
		}

		ServerWorld world = source.getWorld();
		BlockEntity blockEntity = ((BlockEntityProvider) block).createBlockEntity(world);
		if (blockEntity != null)
		{
			if (nbt != null)
			{
				blockEntity.fromTag(nbt);
			}
			world.setBlockEntity(blockPos, blockEntity);
			Messenger.tell(source, tr("create.created", Messenger.blockEntity(blockEntity)));
			return 1;
		}
		else
		{
			Messenger.tell(source,  Messenger.formatting(tr("create.failed"), "r"));
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

		Messenger.tell(source, Messenger.format("- %1$s: %2$sx", name, blockEntities.size()));
		for (int i = 0; i < topN.size(); i++)
		{
			Multiset.Entry<BlockEntityType<?>> entry = topN.get(i);
			Messenger.tell(source, Messenger.format(
					"  %1$s. %2$s: %3$sx",
					i + 1,
					IdentifierUtil.id(entry.getElement()),
					entry.getCount()
			));
		}
	}

	private int showStatistic(ServerCommandSource source)
	{
		ServerWorld world = source.getWorld();
		Messenger.tell(source, tr("statistic.title", Messenger.dimension(DimensionWrapper.of(world))));
		this.showTopNInCollection(source, tr("statistic.total"), world.blockEntities);
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
				).
				then(literal("remove").
						then(argument("pos", blockPos()).
								executes(c -> this.removeAt(c.getSource(), getLoadedBlockPos(c, "pos")))
						)
				).
				then(literal("create").
						then(argument("pos", blockPos()).
								then(argument("block", identifier()).
										suggests(this::suggestBlockWithEntity).
										executes(this::createAt).
										then(argument("data", nbtCompound()).
												executes(this::createAt)
										)
								)
						)
				);
	}

	private CompletableFuture<Suggestions> suggestBlockWithEntity(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder)
	{
		return CommandSource.suggestIdentifiers(
				Registry.BLOCK.stream().
						filter(block -> block instanceof BlockEntityProvider).
						map(Registry.BLOCK::getId),
				builder
		);
	}
}
