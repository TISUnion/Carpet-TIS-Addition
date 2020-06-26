package carpettisaddition.mixins;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import carpettisaddition.CarpetTISAdditionSettings;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(PlayerCommand.class)
public class Carpet_PlayerCommandMixin
{
	private static String getStringWithPrefix(final CommandContext<?> context, final String name)
	{
		String playerName = StringArgumentType.getString(context, name);
		if (!CarpetTISAdditionSettings.fakePlayerNamePrefix.equals(CarpetTISAdditionSettings.fakePlayerNameNone))
		{
			playerName = CarpetTISAdditionSettings.fakePlayerNamePrefix + playerName;
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
		return getStringWithPrefix(context, name);
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
		return getStringWithPrefix(context, name);
	}

	@Inject(
			method = "spawn",
			at = @At(value = "HEAD"),
			remap = false,
			cancellable = true
	)
	private static void checkNameLengthLimit(CommandContext context, CallbackInfoReturnable<Integer> cir)
	{
		String playerName = getStringWithPrefix(context, "player");
		if (playerName.length() > 16)
		{
			Messenger.m((ServerCommandSource)context.getSource(), "rb Player name: " + playerName + " is too long");
			cir.setReturnValue(1);
			cir.cancel();
		}
	}
}
