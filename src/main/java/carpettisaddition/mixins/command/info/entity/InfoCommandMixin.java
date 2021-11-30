package carpettisaddition.mixins.command.info.entity;

import carpet.commands.InfoCommand;
import carpettisaddition.commands.info.entity.EntityInfoCommand;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

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
	private static LiteralArgumentBuilder<ServerCommandSource> registerInfoEntity(LiteralArgumentBuilder<ServerCommandSource> builder)
	{
		EntityInfoCommand.getInstance().extendCommand(builder);
		return builder;
	}
}
