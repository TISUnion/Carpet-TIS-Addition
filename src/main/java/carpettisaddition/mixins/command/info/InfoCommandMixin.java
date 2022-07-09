package carpettisaddition.mixins.command.info;

import carpet.commands.InfoCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 11900
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.command.CommandRegistryAccess;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(InfoCommand.class)
public abstract class InfoCommandMixin
{
	//#if MC >= 11900
	//$$ private static CommandRegistryAccess currentCommandBuildContext$TISCM = null;
 //$$
	//$$ @Inject(method = "register", at = @At("HEAD"), remap = false)
	//$$ private static void storeCommandBuildContext(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext, CallbackInfo ci)
	//$$ {
	//$$ 	currentCommandBuildContext$TISCM = commandBuildContext;
	//$$ }
	//#endif

	@ModifyArg(
			method = "register",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/CommandDispatcher;register(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;"
			),
			index = 0,
			remap = false
	)
	private static LiteralArgumentBuilder<ServerCommandSource> extendsInfoCommand(LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		carpettisaddition.commands.info.InfoCommand.getInstance().extendCommand(
				builder
				//#if MC >= 11900
				//$$ , currentCommandBuildContext$TISCM
				//#endif
		);
		return builder;
	}
}
