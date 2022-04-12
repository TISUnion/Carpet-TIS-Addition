package carpettisaddition.helpers.carpet.shape;

import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import carpettisaddition.mixins.carpet.shape.ExpiringShapeInvoker;
import carpettisaddition.utils.compat.scarpet.ShapeDispatcher;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

public class ShapeHolder<T extends ShapeDispatcher.ExpiringShape>
{
	public final T shape;
	public final Map<String, Value> params;
	public final Map<String, Value> emptyParams;

	public ShapeHolder(T shape, Map<String, Value> params)
	{
		this.shape = shape;
		this.params = params;
		this.emptyParams = Maps.newHashMap(this.params);
		this.emptyParams.put("duration", new NumericValue(0));
		this.updateShape();
	}

	private void updateShape()
	{
		// the shape instance is useful for non-carpet players
		((ExpiringShapeInvoker) this.shape).callInit(this.params);
	}

	public Pair<ShapeDispatcher.ExpiringShape, Map<String, Value>> toPair(boolean display)
	{
		return Pair.of(this.shape, display ? this.params : this.emptyParams);
	}

	public void setValue(String key, Value value)
	{
		this.params.put(key, value);
		this.emptyParams.put(key, value);
		this.updateShape();
	}
}
