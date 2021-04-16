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
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
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

	public void addMarker(PlayerEntity playerEntity, BlockPos blockPos, DyeColor color, @Nullable BaseText name)
	{
		if (playerEntity instanceof ServerPlayerEntity && !playerEntity.world.isClient() && playerEntity.world instanceof ServerWorld)
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
						playerEntity.sendMessage(Messenger.s(String.format(
								this.tr("on_type_switch", "Switch marker to %1$s mode"),
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
				playerEntity.sendMessage(Messenger.s(String.format(
						this.tr("on_unmark", "Unmarked %1$s from MicroTiming logging"),
						TextUtil.getCoordinateString(blockPos)
				)), true);
			}
			if (createNewMarker)
			{
				MicroTimingMarker newMarker = new MicroTimingMarker((ServerWorld)playerEntity.world, blockPos, color, name);
				this.markers.put(key, newMarker);
				newMarker.sendShapeToAll();
				String coord = TextUtil.getCoordinateString(blockPos);
				playerEntity.sendMessage(Messenger.c(
						Messenger.s(String.format(this.tr("on_mark.pre", "Marked %1$s with color "), coord)),
						Messenger.s(color.toString(), MicroTimingUtil.getColorStyle(color)),
						Messenger.s(String.format(this.tr("on_mark.post", " "), coord))
				), true);
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
		return Optional.ofNullable(this.markers.get(new StorageKey(world, blockPos))).map(MicroTimingMarker::getMarkerNameString);
	}

	private void sendMarkersForPlayerInner(ServerPlayerEntity player, boolean display)
	{
		ShapeDispatcher.sendShape(
				Collections.singletonList(player),
				this.markers.values().stream().
						flatMap(marker -> marker.getShapeDataList(display).stream()).
						collect(Collectors.toList())
		);
	}

	public void sendMarkersForPlayer(ServerPlayerEntity player)
	{
		this.sendMarkersForPlayerInner(player, true);
	}

	public void cleanMarkersForPlayer(ServerPlayerEntity player)
	{
		this.sendMarkersForPlayerInner(player, false);
	}

	private static class StorageKey
	{
		private final RegistryKey<World> dimensionType;
		private final BlockPos blockPos;

		private StorageKey(RegistryKey<World> dimensionType, BlockPos blockPos)
		{
			this.dimensionType = dimensionType;
			this.blockPos = blockPos;
		}

		private StorageKey(World world, BlockPos blockPos)
		{
			this(world.getRegistryKey(), blockPos);
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
