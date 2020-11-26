package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class LiteralRemovalReason extends RemovalReason
{
	public static final LiteralRemovalReason DESPAWN = new LiteralRemovalReason("despawn", "Despawn");
	public static final LiteralRemovalReason OTHER = new LiteralRemovalReason("other", "Other");

	// for item entity and xp orb entity
	public static final LiteralRemovalReason MERGE = new LiteralRemovalReason("merge", "Entity merging");
	public static final LiteralRemovalReason PICKUP = new LiteralRemovalReason("pickup", "Entity picked up");

	// for item entity
	public static final LiteralRemovalReason HOPPER = new LiteralRemovalReason("hopper", "Collected by hopper");

	private final String name;
	private final String translationKey;

	private LiteralRemovalReason(String translationKey, String name)
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
