package carpettisaddition.mixins.command.tick.warp;

import carpet.commands.TickCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
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

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
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
	private static LiteralArgumentBuilder<ServerCommandSource> registerTickWarpInfo(LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		TickWarpHUDLogger.getInstance().extendCommand(
				CommandTreeContext.of(
						builder
						//#if MC >= 11900
						//$$ , currentCommandBuildContext$TISCM
						//#endif
				)
		);
		return builder;
	}
}
