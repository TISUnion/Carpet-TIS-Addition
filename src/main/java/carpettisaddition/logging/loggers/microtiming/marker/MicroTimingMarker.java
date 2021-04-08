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

import java.util.Collections;
import java.util.Map;

public class MicroTimingMarker
{
	public final DyeColor color;
	public final ShapeDispatcher.Box box;
	public final Map<String, Value> shapeParams;
	public final Map<String, Value> shapeParamsEmpty;

	@SuppressWarnings("ConstantConditions")
	MicroTimingMarker(ServerWorld serverWorld, BlockPos blockPos, DyeColor color)
	{
		this.shapeParams = Maps.newHashMap();
		this.shapeParams.put("shape", new StringValue("box"));
		this.shapeParams.put("line", new NumericValue(3.0D));
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
