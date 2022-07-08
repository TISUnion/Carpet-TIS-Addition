package carpettisaddition.mixins.rule.fakePlayerName;

import carpet.commands.PlayerCommand;
import carpettisaddition.CarpetTISAdditionSettings;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

//#if MC < 11500
//$$ import carpet.utils.Messenger;
//$$ import net.minecraft.server.command.ServerCommandSource;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	private static String getDecoratedString(final CommandContext<?> context, final String name)
	{
		String rulePrefix = CarpetTISAdditionSettings.fakePlayerNamePrefix;
		String ruleSuffix = CarpetTISAdditionSettings.fakePlayerNameSuffix;
		String playerName = StringArgumentType.getString(context, name);
		if (!rulePrefix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra) && !playerName.startsWith(rulePrefix))
		{
			playerName = rulePrefix + playerName;
		}
		if (!ruleSuffix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra) && !playerName.endsWith(ruleSuffix))
		{
			playerName = playerName + ruleSuffix;
		}
		return playerName;
	}

	@Redirect(
			method = "spawn",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
			),
			require = 2,
			remap = false
	)
	private static String getStringWithPrefixAtSpawn(final CommandContext<?> context, final String name)
	{
		return getDecoratedString(context, name);
	}

	@Redirect(
			method = "cantSpawn",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
			),
			require = 1,
			remap = false
	)
	private static String getStringWithPrefixAtCantSpawn(final CommandContext<?> context, final String name)
	{
		return getDecoratedString(context, name);
	}

	//#if MC >= 11600
	//#elseif MC >= 11500
	@ModifyConstant(
			method = "spawn",
			constant = @Constant(intValue = 40),
			require = 1,
			remap = false
	)
	private static int nameLengthLimit(int value)
	{
		return 16;
	}
	//#else
	//$$ @Inject(
	//$$		method = "spawn",
	//$$ 		at = @At(value = "HEAD"),
	//$$ 		remap = false,
	//$$ 		cancellable = true
	//$$ )
	//$$ private static void checkNameLengthLimit(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir)
	//$$ {
	//$$ 	String playerName = getDecoratedString(context, "player");
	//$$ 	if (playerName.length() > 16)
	//$$ 	{
	//$$ 		Messenger.m(context.getSource(), "rb Player name: " + playerName + " is too long");
	//$$ 		cir.setReturnValue(1);
	//$$ 	}
	//$$ }
	//#endif
}
