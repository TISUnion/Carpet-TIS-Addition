package carpettisaddition.mixins.carpet.commands.spawnTrackingRestart;

import carpet.commands.SpawnCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnCommand.class)
public interface SpawnCommandInvoker
{
	@Invoker(remap = false)
	static int callStartTracking(ServerCommandSource source, BlockPos a, BlockPos b)
	{
		return 0;
	}

	@Invoker(remap = false)
	static int callStopTracking(ServerCommandSource source)
	{
		return 0;
	}
}
