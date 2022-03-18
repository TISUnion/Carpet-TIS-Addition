package carpettisaddition.commands.manipulate.container;

import carpettisaddition.commands.manipulate.ManipulateCommand;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.class_7157;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

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

	protected BaseText getName()
	{
		return tr("name");
	}

	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode(class_7157 commandBuildContext)
	{
		return literal(this.commandPrefix).executes(c -> this.showHelp(c.getSource()));
	}

	protected int showHelp(ServerCommandSource source)
	{
		Messenger.tell(source, basicTranslator.tr("help", this.getName()));
		return 1;
	}
}
