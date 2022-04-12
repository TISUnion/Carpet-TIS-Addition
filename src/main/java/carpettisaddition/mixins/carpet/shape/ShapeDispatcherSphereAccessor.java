package carpettisaddition.mixins.carpet.shape;

import carpettisaddition.utils.compact.scarpet.ShapeDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ShapeDispatcher.Sphere.class)
public interface ShapeDispatcherSphereAccessor
{
	@Invoker("<init>")
	static ShapeDispatcher.Sphere invokeConstructor()
	{
		throw new AssertionError();
	}
}
