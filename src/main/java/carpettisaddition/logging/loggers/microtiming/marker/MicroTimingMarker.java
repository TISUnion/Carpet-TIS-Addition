package carpettisaddition.logging.loggers.microtiming.marker;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import carpet.script.value.Value;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.mixins.carpet.shape.ExpiringShapeInvoker;
import carpettisaddition.mixins.logger.microtiming.marker.DyeColorAccessor;
import com.google.common.collect.Maps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class MicroTimingMarker
{
	public final DyeColor color;
	public final ShapeDispatcher.Box box;
	public final Map<String, Value> shapeParams;
	public final Map<String, Value> shapeParamsEmpty;
	private MicroTimingMarkerType markerType;
	@Nullable
	private String name;

	@SuppressWarnings("ConstantConditions")
	public MicroTimingMarker(ServerWorld serverWorld, BlockPos blockPos, DyeColor color)
	{
		this.markerType = MicroTimingMarkerType.REGULAR;
		this.shapeParams = Maps.newHashMap();
		this.shapeParams.put("shape", new StringValue("box"));
		this.color = color;
		this.shapeParams.put("color", new NumericValue(((long) ((DyeColorAccessor) (Object) this.color).getTextColor() << 8) | 0xAF));
		this.shapeParams.put("dim", new StringValue(serverWorld.getDimension().getType().toString()));
		this.shapeParams.put("duration", new NumericValue(Integer.MAX_VALUE));
		this.shapeParams.put("from", listFromBlockPos(blockPos));
		this.shapeParams.put("to", listFromBlockPos(blockPos.add(1, 1, 1)));
		this.box = new ShapeDispatcher.Box();
		((ExpiringShapeInvoker) this.box).callInit(this.shapeParams);
		this.shapeParamsEmpty = Maps.newHashMap(this.shapeParams);
		this.shapeParamsEmpty.put("duration", new NumericValue(0));
		this.updateLineWidth();
	}

	public void setName(@Nullable String name)
	{
		this.name = name;
	}

	@Nullable
	public String getName()
	{
		return this.name;
	}

	public MicroTimingMarkerType getMarkerType()
	{
		return this.markerType;
	}

	private void updateLineWidth()
	{
		this.shapeParams.put("line", new NumericValue(this.markerType.getLineWidth()));
		this.shapeParamsEmpty.put("line", new NumericValue(this.markerType.getLineWidth()));
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

	private void sendShape(Map<String, Value> params)
	{
		ShapeDispatcher.sendShape(MicroTimingUtil.getSubscribedPlayers(), Collections.singletonList(Pair.of(this.box, params)));
	}

	public void sendShapeToAll()
	{
		this.sendShape(this.shapeParams);
	}

	public void cleanShapeToAll()
	{
		this.sendShape(this.shapeParamsEmpty);
	}
}
