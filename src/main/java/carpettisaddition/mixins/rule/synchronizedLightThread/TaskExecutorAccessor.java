package carpettisaddition.mixins.rule.synchronizedLightThread;

import net.minecraft.util.thread.TaskExecutor;
import net.minecraft.util.thread.TaskQueue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TaskExecutor.class)
public interface TaskExecutorAccessor<T>
{
	@Accessor
	TaskQueue<? super T, ? extends Runnable> getQueue();
}
