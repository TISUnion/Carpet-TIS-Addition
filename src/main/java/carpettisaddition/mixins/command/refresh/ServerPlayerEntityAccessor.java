package carpettisaddition.mixins.command.refresh;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900 && MC < 11901
//$$ import net.minecraft.network.message.MessageType;
//$$ import net.minecraft.util.registry.RegistryKey;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor
{
	// TODO: drop support for 1.19 and remove this class when 1.19.1 releases
	//#if MC >= 11900 && MC < 11901
	//$$ @Invoker
	//$$ int invokeGetMessageTypeId(RegistryKey<MessageType> registryKey);
	//#endif
}