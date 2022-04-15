package carpettisaddition.mixins.command.mobcapsLocal;

import carpet.commands.SpawnCommand;
import carpettisaddition.commands.spawn.mobcapsLocal.MobcapsLocalCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
	private static CommandRegistryAccess currentCommandBuildContext$CTA = null;

	@Inject(method = "register", at = @At("HEAD"), remap = false)
	private static void storeCommandBuildContext(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext, CallbackInfo ci)
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
	private static LiteralArgumentBuilder<ServerCommandSource> appendLocalArgumentOnSpawnMobcaps(LiteralArgumentBuilder<ServerCommandSource> rootNode)
	{
		MobcapsLocalCommand.getInstance().extendCommand(rootNode, currentCommandBuildContext$CTA);
		return rootNode;
	}
}
