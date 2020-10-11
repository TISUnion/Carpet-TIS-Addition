package carpettisaddition.mixins.rule.fakePlayerName;

import carpet.commands.PlayerCommand;
import carpettisaddition.CarpetTISAdditionSettings;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(PlayerCommand.class)
public abstract class PlayerCommandMixin
{
	private static String getDecoratedString(final CommandContext<?> context, final String name)
	{
		String playerName = StringArgumentType.getString(context, name);
		if (!CarpetTISAdditionSettings.fakePlayerNamePrefix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra))
		{
			playerName = CarpetTISAdditionSettings.fakePlayerNamePrefix + playerName;
		}
		if (!CarpetTISAdditionSettings.fakePlayerNameSuffix.equals(CarpetTISAdditionSettings.fakePlayerNameNoExtra))
		{
			playerName = playerName + CarpetTISAdditionSettings.fakePlayerNameSuffix;
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
}
