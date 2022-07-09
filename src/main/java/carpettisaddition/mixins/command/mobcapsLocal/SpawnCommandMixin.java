package carpettisaddition.mixins.command.mobcapsLocal;

import carpet.commands.SpawnCommand;
import carpettisaddition.commands.spawn.mobcapsLocal.MobcapsLocalCommand;
import carpettisaddition.utils.ModIds;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(SpawnCommand.class)
public abstract class SpawnCommandMixin
{
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
		MobcapsLocalCommand.getInstance().extendCommand(rootNode);
		return rootNode;
	}
}