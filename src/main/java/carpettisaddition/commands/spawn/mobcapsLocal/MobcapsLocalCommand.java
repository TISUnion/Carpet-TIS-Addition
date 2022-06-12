package carpettisaddition.commands.spawn.mobcapsLocal;

import carpettisaddition.commands.CommandExtender;
import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
import carpettisaddition.mixins.command.mobcapsLocal.SpawnCommandAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobcapsLocalCommand extends TranslationContext implements CommandExtender
{
	private static final MobcapsLocalCommand INSTANCE = new MobcapsLocalCommand();

	private MobcapsLocalCommand()
	{
		super("command.spawn.mobcapsLocal");
	}

	public static MobcapsLocalCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder, CommandRegistryAccess commandBuildContext)
	{
		builder.then(literal("mobcapsLocal").
				executes(c -> showLocalMobcaps(c.getSource(), c.getSource().getPlayerOrThrow())).
				then(argument("player", EntityArgumentType.player()).
						executes(c -> showLocalMobcaps(c.getSource(), getPlayer(c, "player")))
				)
		);
	}

	private int showLocalMobcaps(ServerCommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException
	{
		int[] ret = new int[1];
		MobcapsLocalLogger.getInstance().withLocalMobcapContext(
				targetPlayer,
				() -> {
					Messenger.tell(source, tr("info", targetPlayer.getDisplayName()));
					ret[0] = SpawnCommandAccessor.invokeGeneralMobcaps(source);
				},
				() -> ret[0] = 0
		);
		return ret[0];
	}
}