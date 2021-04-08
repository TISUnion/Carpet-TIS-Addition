package carpettisaddition.logging.loggers.microtiming.marker;

import carpet.script.utils.ShapeDispatcher;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class MicroTimingMarkerManager extends TranslatableBase
{
	private static final MicroTimingMarkerManager INSTANCE = new MicroTimingMarkerManager();

	private final Map<StorageKey, MicroTimingMarker> markers = Maps.newHashMap();

	public MicroTimingMarkerManager()
	{
		super(MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("marker"));
	}

	public static MicroTimingMarkerManager getInstance()
	{
		return INSTANCE;
	}

	public void clear()
	{
		this.markers.values().forEach(MicroTimingMarker::cleanShapeToAll);
		this.markers.clear();
	}

	public void addMarker(PlayerEntity playerEntity, BlockPos blockPos, DyeColor color)
	{
		if (playerEntity instanceof ServerPlayerEntity && playerEntity.world instanceof ServerWorld)
		{
			StorageKey key = new StorageKey(playerEntity.world, blockPos);
			MicroTimingMarker existedMarker = this.markers.remove(key);
			if (existedMarker != null)
			{
				existedMarker.cleanShapeToAll();
			}
			MicroTimingMarker newMarker = new MicroTimingMarker((ServerWorld)playerEntity.world, blockPos, color);
			if (existedMarker == null || newMarker.color != existedMarker.color)
			{
				this.markers.put(key, newMarker);
				newMarker.sendShapeToAll();
				playerEntity.addChatMessage(Messenger.s(String.format(
						this.tr("on_mark", "Marked %1$s with color %2$s for MicroTiming logging"),
						TextUtil.getCoordinateString(blockPos),
						TextUtil.parseCarpetStyle(MicroTimingUtil.getColorStyle(color)).getColor() + color.toString() + Formatting.RESET
				)), true);
			}
			else
			{
				playerEntity.addChatMessage(Messenger.s(String.format(
						this.tr("on_unmark", "Unmarked %1$s from MicroTiming logging"),
						TextUtil.getCoordinateString(blockPos)
				)), true);
			}
		}
	}

	public Optional<DyeColor> getColor(World world, BlockPos blockPos)
	{
		return Optional.ofNullable(this.markers.get(new StorageKey(world, blockPos))).map(marker -> marker.color);
	}

	public void sendMarkersForPlayer(ServerPlayerEntity player)
	{
		ShapeDispatcher.sendShape(
				Collections.singletonList(player),
				this.markers.values().stream().
						map(marker -> Pair.of((ShapeDispatcher.ExpiringShape)marker.box, marker.shapeParams)).
						collect(Collectors.toList())
		);
	}

	public void cleanMarkersForPlayer(ServerPlayerEntity player)
	{
		ShapeDispatcher.sendShape(
				Collections.singletonList(player),
				this.markers.values().stream().
						map(marker -> Pair.of((ShapeDispatcher.ExpiringShape)marker.box, marker.shapeParamsEmpty)).
						collect(Collectors.toList())
		);
	}

	private static class StorageKey
	{
		private final DimensionType dimensionType;
		private final BlockPos blockPos;

		private StorageKey(DimensionType dimensionType, BlockPos blockPos)
		{
			this.dimensionType = dimensionType;
			this.blockPos = blockPos;
		}

		private StorageKey(World world, BlockPos blockPos)
		{
			this(world.getDimension().getType(), blockPos);
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			StorageKey that = (StorageKey) o;
			return Objects.equals(dimensionType, that.dimensionType) && Objects.equals(blockPos, that.blockPos);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(dimensionType, blockPos);
		}
	}
}
