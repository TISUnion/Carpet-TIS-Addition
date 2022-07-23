package carpettisaddition.mixins.command.info.entity;

import carpet.commands.InfoCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.info.entity.EntityInfoCommand;
import carpettisaddition.utils.ModIds;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11900
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

/**
 * Fabric carpet removed the "/info entity" command tree branch in mc 1.17+
 * so we need to manually mixin and make a new one
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(InfoCommand.class)
public abstract class InfoCommandMixin
{
	@ModifyVariable(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			ordinal = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<ServerCommandSource> registerInfoEntity(
			LiteralArgumentBuilder<ServerCommandSource> builder
			//#if MC >= 11900
			//$$ , CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		EntityInfoCommand.getInstance().extendCommand(
				CommandTreeContext.of(
						builder
						//#if MC >= 11900
						//$$ , commandBuildContext
						//#endif
				)
		);
		return builder;
	}
}