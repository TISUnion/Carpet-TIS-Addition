package carpettisaddition.mixins.carpet.shape;

import carpet.script.utils.ShapeDispatcher;
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
