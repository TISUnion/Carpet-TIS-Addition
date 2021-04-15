package carpettisaddition.mixins.carpet.shape;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.value.Value;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(ShapeDispatcher.ExpiringShape.class)
public interface ExpiringShapeInvoker
{
	@Invoker(remap = false)
	void callInit(Map<String, Value> options);
}
