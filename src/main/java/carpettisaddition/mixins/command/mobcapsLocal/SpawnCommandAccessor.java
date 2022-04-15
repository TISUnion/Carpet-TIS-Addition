package carpettisaddition.mixins.command.mobcapsLocal;

import carpet.commands.SpawnCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnCommand.class)
public interface SpawnCommandAccessor
{
	@Invoker
	static int invokeGeneralMobcaps(ServerCommandSource source)
	{
		throw new RuntimeException();
	}
}
