package carpettisaddition.mixins.command.mobcapsLocal;

import carpet.commands.SpawnCommand;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(SpawnCommand.class)
public interface SpawnCommandAccessor
{
	@Invoker
	static int invokeGeneralMobcaps(ServerCommandSource source)
	{
		throw new RuntimeException();
	}
}