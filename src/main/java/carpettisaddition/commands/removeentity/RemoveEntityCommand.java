package carpettisaddition.commands.removeentity;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.command.argument.EntityArgumentType.entities;
import static net.minecraft.command.argument.EntityArgumentType.getEntities;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveEntityCommand extends AbstractCommand
{
	private static final String NAME = "removeentity";
	private static final RemoveEntityCommand INSTANCE = new RemoveEntityCommand();

	private RemoveEntityCommand()
	{
		super(NAME);
	}

	public static RemoveEntityCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> node = literal(NAME).
				requires(
						player -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRemoveEntity)
				).
				then(argument("target", entities()).
						executes(c -> removeEntities(c.getSource(), getEntities(c, "target")))
				);

		dispatcher.register(node);
	}

	private int removeEntities(ServerCommandSource source, Collection<? extends Entity> entities)
	{
		List<? extends Entity> nonPlayerEntities = entities.stream().
				filter(entity -> !(entity instanceof PlayerEntity)).
				collect(Collectors.toList());
		nonPlayerEntities.forEach(Entity::discard);
		Messenger.tell(source, tr("success", nonPlayerEntities.size()), true);
		return nonPlayerEntities.size();
	}
}
