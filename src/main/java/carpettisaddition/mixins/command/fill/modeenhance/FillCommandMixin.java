package carpettisaddition.mixins.command.fill.modeenhance;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.fill.modeenhance.FillSoftReplaceCommand;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.FillCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

//#if MC >= 11900
//$$ import com.mojang.brigadier.CommandDispatcher;
//$$ import net.minecraft.command.CommandRegistryAccess;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Mixin(FillCommand.class)
public class FillCommandMixin
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
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=destroy",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
					remap = false,
					ordinal = 1
			)
	)
	private static ArgumentBuilder<ServerCommandSource, ?> registerSoftReplaceFillMode(ArgumentBuilder<ServerCommandSource, ?> node)
	{
		FillSoftReplaceCommand.getInstance().extendCommand(
				CommandTreeContext.of(
						node
						//#if MC >= 11900
						//$$ , currentCommandBuildContext$TISCM
						//#endif
				)
		);
		return node;
	}
}
