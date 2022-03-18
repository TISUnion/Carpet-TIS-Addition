package carpettisaddition.commands.info.entity;

import carpet.utils.Messenger;
import carpettisaddition.commands.CommandExtender;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_7157;
import net.minecraft.command.argument.EntityArgumentType;
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
 */
public class EntityInfoCommand implements CommandExtender
{
	private static final EntityInfoCommand INSTANCE = new EntityInfoCommand();

	public static EntityInfoCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(LiteralArgumentBuilder<ServerCommandSource> builder, class_7157 commandBuildContext)
	{
		builder.then(literal("entity").
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
		Messenger.m(source, "");
		Messenger.send(source, actual);
	}
}
