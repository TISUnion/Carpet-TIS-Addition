package carpettisaddition.commands.lifetime.spawning;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class LiteralSpawningReason extends SpawningReason
{
	public static final LiteralSpawningReason NATURAL = new LiteralSpawningReason("natural", "Natural spawning");
	public static final LiteralSpawningReason PORTAL_PIGMAN = new LiteralSpawningReason("portal_pigman", "Nether portal pigman spawning");
	public static final LiteralSpawningReason COMMAND = new LiteralSpawningReason("command", "Summon command");
	public static final LiteralSpawningReason ITEM = new LiteralSpawningReason("item", "Spawned by item");

	private final String translationKey;
	private final String name;

	private LiteralSpawningReason(String translationKey, String name)
	{
		this.translationKey = translationKey;
		this.name = name;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.s(this.tr(this.translationKey, this.name));
	}
}
