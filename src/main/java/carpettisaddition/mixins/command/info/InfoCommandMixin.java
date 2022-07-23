package carpettisaddition.mixins.command.info;

import carpet.commands.InfoCommand;
import carpettisaddition.commands.CommandTreeContext;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11900
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.command.CommandRegistryAccess;
//#endif

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
	private static LiteralArgumentBuilder<ServerCommandSource> extendsInfoCommand(
			LiteralArgumentBuilder<ServerCommandSource> builder
			//#if MC >= 11900
			//$$ , CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext
			//#endif
	)
	{
		carpettisaddition.commands.info.InfoCommand.getInstance().extendCommand(
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
