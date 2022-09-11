package carpettisaddition.commands.spawn.mobcapsLocal;

import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;

// a placeholder class, its implementation is in 1.18+
public class MobcapsLocalCommand extends AbstractCommand implements CommandExtender
{
	private static final MobcapsLocalCommand INSTANCE = new MobcapsLocalCommand();

	private MobcapsLocalCommand()
	{
		super("spawn.mobcapsLocal");
	}

	public static MobcapsLocalCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
	}
}