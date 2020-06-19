package carpettisaddition.mixins;

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
public class CarpetPlayerCommandMixin
{
	@Redirect(
			method = {"spawn", "cantSpawn"},
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/brigadier/arguments/StringArgumentType;getString(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;)Ljava/lang/String;"
			),
			require = 2,
			remap = false
	)
	private static String getStringWithPrefix(final CommandContext<?> context, final String name)
	{
		String playerName = StringArgumentType.getString(context, name);
		if (!CarpetTISAdditionSettings.fakePlayerNamePrefix.equals(CarpetTISAdditionSettings.fakePlayerNameNone))
		{
			playerName = CarpetTISAdditionSettings.fakePlayerNamePrefix + playerName;
		}
		return playerName;
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
