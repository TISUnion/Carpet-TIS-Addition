package carpettisaddition.commands.manipulate.container;

import carpettisaddition.commands.manipulate.ManipulateCommand;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class AbstractContainerController extends TranslationContext
{
	protected static final Translator basicTranslator = ManipulateCommand.getInstance().getTranslator().getDerivedTranslator("container");
	protected final String commandPrefix;

	public AbstractContainerController(String translationName)
	{
		super(basicTranslator.getDerivedTranslator(translationName));
		this.commandPrefix = translationName.replace("_", "");
	}

	protected MutableText getName()
	{
		return tr("name");
	}

	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode(CommandRegistryAccess commandBuildContext)
	{
		return literal(this.commandPrefix).executes(c -> this.showHelp(c.getSource()));
	}

	protected int showHelp(ServerCommandSource source)
	{
		Messenger.tell(source, basicTranslator.tr("help", this.getName()));
		return 1;
	}
}
