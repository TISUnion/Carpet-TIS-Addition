package carpettisaddition.mixins.command.refresh;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900
//$$ import net.minecraft.network.message.MessageType;
//$$ import net.minecraft.util.registry.RegistryKey;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor
{
	//#if MC >= 11900
	//$$ @Invoker
	//$$ int invokeGetMessageTypeId(RegistryKey<MessageType> registryKey);
	//#endif
}