package carpettisaddition.mixins.command.refresh;

import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor
{
	@Invoker
	int invokeGetMessageTypeId(RegistryKey<MessageType> registryKey);
}
