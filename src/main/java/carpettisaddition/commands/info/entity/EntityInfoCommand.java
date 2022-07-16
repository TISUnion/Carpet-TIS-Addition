package carpettisaddition.commands.info.entity;

import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.Messenger;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * Logic ports from fabric-carpet 1.4.54
 * Newer carpet remove entity info framework, so we need to keep a copy of that
 * Only used in mc1.17+
 */
public class EntityInfoCommand implements CommandExtender
{
	private static final EntityInfoCommand INSTANCE = new EntityInfoCommand();

	public static EntityInfoCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Extend context)
	{
		context.node.then(literal("entity").
				then(argument("entity selector", EntityArgumentType.entities()).
						executes((c) -> infoEntities(
								c.getSource(), EntityArgumentType.getEntities(c, "entity selector"), null)
						).
						then(literal("grep").
								then(argument("regexp", greedyString()).
										executes((c) -> infoEntities(
												c.getSource(),
												EntityArgumentType.getEntities(c, "entity selector"),
												getString(c, "regexp")
										))
								)
						)
				)
		);
	}

	private static int infoEntities(ServerCommandSource source, Collection<? extends Entity> entities, String grep)
	{
		for (Entity e : entities)
		{
			List<BaseText> report = EntityInfoPorting.entityInfo(e, source.getWorld());
			printEntity(report, source, grep);
		}
		return 1;
	}

	private static void printEntity(List<BaseText> messages, ServerCommandSource source, String grep)
	{
		List<BaseText> actual = new ArrayList<>();
		if (grep != null)
		{
			Pattern p = Pattern.compile(grep);
			actual.add(messages.get(0));
			boolean empty = true;
			for (int i = 1; i < messages.size(); i++)
			{
				BaseText line = messages.get(i);
				Matcher m = p.matcher(line.getString());
				if (m.find())
				{
					empty = false;
					actual.add(line);
				}
			}
			if (empty)
			{
				return;
			}
		}
		else
		{
			actual = messages;
		}
		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, actual);
	}
}