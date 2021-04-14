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
import org.jetbrains.annotations.Nullable;

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

	public void addMarker(PlayerEntity playerEntity, BlockPos blockPos, DyeColor color, @Nullable String name)
	{
		if (playerEntity instanceof ServerPlayerEntity && playerEntity.world instanceof ServerWorld)
		{
			StorageKey key = new StorageKey(playerEntity.world, blockPos);
			MicroTimingMarker existedMarker = this.markers.get(key);
			boolean removeExistedMarker = false;
			boolean createNewMarker = false;
			if (existedMarker != null)
			{
				// roll the marker type to the next one
				if (existedMarker.color == color)
				{
					// has next marker type
					if (existedMarker.rollMarkerType())
					{
						existedMarker.setName(name);
						playerEntity.addChatMessage(Messenger.s(String.format(
								this.tr("on_type_roll", "Rolled marker type to %1$s"),
								existedMarker.getMarkerType()
						)), true);
					}
					// no more marker type, remove it
					else
					{
						removeExistedMarker = true;
					}
				}
				// color is different, just remove it and create a new one
				else
				{
					removeExistedMarker = true;
					createNewMarker = true;
				}
			}
			// no existed marker, create a new one
			else
			{
				createNewMarker = true;
			}

			if (removeExistedMarker)
			{
				existedMarker.cleanShapeToAll();
				this.markers.remove(key);
				playerEntity.addChatMessage(Messenger.s(String.format(
						this.tr("on_unmark", "Unmarked %1$s from MicroTiming logging"),
						TextUtil.getCoordinateString(blockPos)
				)), true);
			}
			if (createNewMarker)
			{
				MicroTimingMarker newMarker = new MicroTimingMarker((ServerWorld)playerEntity.world, blockPos, color);
				newMarker.setName(name);
				this.markers.put(key, newMarker);
				newMarker.sendShapeToAll();
				playerEntity.addChatMessage(Messenger.s(String.format(
						this.tr("on_mark", "Marked %1$s with color %2$s"),
						TextUtil.getCoordinateString(blockPos),
						TextUtil.parseCarpetStyle(MicroTimingUtil.getColorStyle(color)).getColor() + color.toString() + Formatting.RESET,
						newMarker.getMarkerType()
				)), true);
			}
		}
	}

	public Optional<DyeColor> getColor(World world, BlockPos blockPos, MicroTimingMarkerType requiredMarkerType)
	{
		MicroTimingMarker marker = this.markers.get(new StorageKey(world, blockPos));
		if (marker == null)
		{
			return Optional.empty();
		}
		return Optional.ofNullable(marker.getMarkerType().ordinal() >= requiredMarkerType.ordinal() ? marker.color : null);
	}

	public Optional<String> getMarkerName(World world, BlockPos blockPos)
	{
		return Optional.ofNullable(this.markers.get(new StorageKey(world, blockPos))).map(MicroTimingMarker::getName);
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
