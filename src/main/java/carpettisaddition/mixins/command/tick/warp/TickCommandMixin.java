package carpettisaddition.mixins.command.tick.warp;

import carpet.commands.TickCommand;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_7157;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TickCommand.class)
public abstract class TickCommandMixin
{
	private static class_7157 currentCommandBuildContext$CTA = null;

	@Inject(method = "register", at = @At("HEAD"), remap = false)
	private static void storeCommandBuildContext(CommandDispatcher<ServerCommandSource> dispatcher, class_7157 commandBuildContext, CallbackInfo ci)
	{
		currentCommandBuildContext$CTA = commandBuildContext;
	}

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
		TickWarpHUDLogger.getInstance().extendCommand(builder, currentCommandBuildContext$CTA);
		return builder;
	}
}
