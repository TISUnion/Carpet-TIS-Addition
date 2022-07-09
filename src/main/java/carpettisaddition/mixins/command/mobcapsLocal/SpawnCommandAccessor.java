package carpettisaddition.mixins.command.mobcapsLocal;

import carpet.commands.SpawnCommand;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11800
//$$ import net.minecraft.server.command.ServerCommandSource;
//$$ import org.spongepowered.asm.mixin.gen.Invoker;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(SpawnCommand.class)
public interface SpawnCommandAccessor
{
	//#if MC >= 11800
	//$$ @Invoker
	//$$ static int invokeGeneralMobcaps(ServerCommandSource source)
	//$$ {
	//$$ 	throw new RuntimeException();
	//$$ }
	//#endif
}