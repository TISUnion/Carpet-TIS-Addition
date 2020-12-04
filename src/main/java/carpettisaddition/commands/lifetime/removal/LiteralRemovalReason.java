package carpettisaddition.commands.lifetime.removal;

import carpet.utils.Messenger;
import net.minecraft.text.BaseText;

public class LiteralRemovalReason extends RemovalReason
{
	// 32m ~ 128m randomly despawn
	public static final LiteralRemovalReason DESPAWN_RANDOMLY = new LiteralRemovalReason("despawn.randomly", "Randomly despawn");
	// > 128m immediately despawn
	public static final LiteralRemovalReason DESPAWN_IMMEDIATELY = new LiteralRemovalReason("despawn.immediately", "Immediately despawn");
	// difficulty peaceful
	public static final LiteralRemovalReason DESPAWN_DIFFICULTY = new LiteralRemovalReason("despawn.difficulty", "Despawn for difficulty");
	// item/xp orb timeout
	public static final LiteralRemovalReason DESPAWN_TIMEOUT = new LiteralRemovalReason("despawn.timeout", "Despawn for timeout");

	// when the persistent tag set to true, treat it as removed since it doesn't count towards mobcaps anymore
	public static final LiteralRemovalReason PERSISTENT = new LiteralRemovalReason("persistent", "Becomes persistent");

	// the fallback reason
	public static final LiteralRemovalReason OTHER = new LiteralRemovalReason("other", "Other");

	// for item entity and xp orb entity
	public static final LiteralRemovalReason MERGE = new LiteralRemovalReason("merge", "Entity merging");
	public static final LiteralRemovalReason PICKUP = new LiteralRemovalReason("pickup", "Picked up by player");

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
