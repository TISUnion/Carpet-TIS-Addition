package carpettisaddition.commands.manipulate.container.controller;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.ManipulateCommand;
import carpettisaddition.commands.manipulate.container.ContainerManipulator;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import static net.minecraft.server.command.CommandManager.literal;

public abstract class AbstractContainerController extends TranslationContext
{
	protected static final Translator basicTranslator = ContainerManipulator.getInstance().getTranslator();
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

	public ArgumentBuilder<ServerCommandSource, ?> getCommandNode(CommandTreeContext context)
	{
		return literal(this.commandPrefix).executes(c -> this.showHelp(c.getSource()));
	}

	protected int showHelp(ServerCommandSource source)
	{
		Messenger.tell(source, basicTranslator.tr("help", this.getName()));
		return 1;
	}
}
