package carpettisaddition.logging.loggers.microtiming.marker;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.*;
import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.marker.texthack.ScarpetDisplayedTextHack;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.carpet.shape.ExpiringShapeInvoker;
import carpettisaddition.mixins.logger.microtiming.marker.DyeColorAccessor;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class MicroTimingMarker
{
	public final DyeColor color;
	private final ShapeData<ShapeDispatcher.Box> box;
	private final ShapeData<ShapeDispatcher.Text> text;
	private MicroTimingMarkerType markerType;
	@Nullable
	private final BaseText markerName;

	@SuppressWarnings("ConstantConditions")
	public MicroTimingMarker(ServerWorld serverWorld, BlockPos blockPos, DyeColor color, @Nullable BaseText markerName)
	{
		this.color = color;
		this.markerName = markerName;
		this.markerType = MicroTimingMarkerType.REGULAR;
		Map<String, Value> boxParams = Maps.newHashMap();
		boxParams.put("shape", new StringValue("box"));
		boxParams.put("color", new NumericValue(((long) ((DyeColorAccessor) (Object) this.color).getTextColor() << 8) | 0xAF));
		boxParams.put("dim", new StringValue(serverWorld.getDimension().getType().toString()));
		boxParams.put("duration", new NumericValue(Integer.MAX_VALUE));
		boxParams.put("from", listFromBlockPos(blockPos));
		boxParams.put("to", listFromBlockPos(blockPos.add(1, 1, 1)));
		this.box = new ShapeData<>(new ShapeDispatcher.Box(), boxParams);
		if (this.markerName != null)
		{
			Map<String, Value> textParams = Maps.newHashMap();
			textParams.put("shape", new StringValue("label"));
			textParams.put("dim", new StringValue(serverWorld.getDimension().getType().toString()));
			textParams.put("duration", new NumericValue(Integer.MAX_VALUE));
			textParams.put("pos", ListValue.of(new NumericValue(blockPos.getX() + 0.5D), new NumericValue(blockPos.getY() + 0.5D), new NumericValue(blockPos.getZ() + 0.5D)));
			textParams.put("text", new FormattedTextValue(Messenger.c(Messenger.s(TextUtil.parseCarpetStyle(MicroTimingUtil.getColorStyle(this.color)).getColor() + "# " + Formatting.RESET), TextUtil.copyText(this.markerName))));
			textParams.put("align", new StringValue(ScarpetDisplayedTextHack.MICRO_TIMING_TEXT_MAGIC_STRING));
			this.text = new ShapeData<>(new ShapeDispatcher.Text(), textParams);
		}
		else
		{
			this.text = null;
		}
		this.updateLineWidth();
	}

	@Nullable
	public String getMarkerNameString()
	{
		return this.markerName != null ? this.markerName.asString() : null;
	}

	public MicroTimingMarkerType getMarkerType()
	{
		return this.markerType;
	}

	private void updateLineWidth()
	{
		this.box.updateLineWidth(this.markerType.getLineWidth());
	}

	public boolean rollMarkerType()
	{
		boolean hasNext;
		switch (this.markerType)
		{
			case REGULAR:
				this.markerType = MicroTimingMarkerType.END_ROD;
				hasNext = true;
				break;
			case END_ROD:
			default:
				hasNext = false;
				break;
		}
		if (hasNext)
		{
			this.cleanShapeToAll();
			this.updateLineWidth();
			this.sendShapeToAll();
		}
		return hasNext;
	}

	private static ListValue listFromBlockPos(BlockPos blockPos)
	{
		return ListValue.of(new NumericValue(blockPos.getX()), new NumericValue(blockPos.getY()), new NumericValue(blockPos.getZ()));
	}

	public List<Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>> getShapeDataList(boolean display)
	{
		List<Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>>> result = Lists.newArrayList();
		result.add(this.box.toPair(display));
		if (this.text != null)
		{
			result.add(this.text.toPair(display));
		}
		return result;
	}

	public void sendShapeToAll()
	{
		ShapeDispatcher.sendShape(MicroTimingUtil.getSubscribedPlayers(), this.getShapeDataList(true));
	}

	public void cleanShapeToAll()
	{
		ShapeDispatcher.sendShape(MicroTimingUtil.getSubscribedPlayers(), this.getShapeDataList(false));
	}

	private static class ShapeData<T extends ShapeDispatcher.ExpiringShape>
	{
		public final T shape;
		public final Map<String, Value> params;
		public final Map<String, Value> emptyParams;

		private ShapeData(T shape, Map<String, Value> params)
		{
			this.shape = shape;
			this.params = params;
			((ExpiringShapeInvoker)this.shape).callInit(this.params);
			this.emptyParams = Maps.newHashMap(this.params);
			this.emptyParams.put("duration", new NumericValue(0));
		}

		private Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>> toPair(boolean display)
		{
			return Pair.of(this.shape, display ? this.params : this.emptyParams);
		}

		public void updateLineWidth(float lineWidth)
		{
			this.params.put("line", new NumericValue(lineWidth));
			this.emptyParams.put("line", new NumericValue(lineWidth));
		}
	}
}
