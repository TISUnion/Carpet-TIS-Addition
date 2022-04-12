package carpettisaddition.mixins.carpet.shape;

import carpettisaddition.utils.compat.scarpet.ShapeDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShapeDispatcher.Line.class)
public interface ShapeDispatcherLineAccessor
{
	@Invoker("<init>")
	static ShapeDispatcher.Line invokeConstructor()
	{
		throw new AssertionError();
	}
}
