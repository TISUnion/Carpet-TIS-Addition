package carpettisaddition.commands.lifetime.removal;

import net.minecraft.text.BaseText;

public class LiteralRemovalReason extends RemovalReason
{
	// 32m ~ 128m randomly despawn
	public static final LiteralRemovalReason DESPAWN_RANDOMLY = new LiteralRemovalReason("despawn.randomly");
	// > 128m immediately despawn
	public static final LiteralRemovalReason DESPAWN_IMMEDIATELY = new LiteralRemovalReason("despawn.immediately");
	// difficulty peaceful
	public static final LiteralRemovalReason DESPAWN_DIFFICULTY = new LiteralRemovalReason("despawn.difficulty");
	// item/xp orb timeout
	public static final LiteralRemovalReason DESPAWN_TIMEOUT = new LiteralRemovalReason("despawn.timeout");

	// when the persistent tag set to true, treat it as removed since it doesn't count towards mobcaps anymore
	public static final LiteralRemovalReason PERSISTENT = new LiteralRemovalReason("persistent");

	// the fallback reason
	public static final LiteralRemovalReason OTHER = new LiteralRemovalReason("other");

	// for item entity and xp orb entity
	public static final LiteralRemovalReason MERGE = new LiteralRemovalReason("merge");

	// for item entity
	public static final LiteralRemovalReason HOPPER = new LiteralRemovalReason("hopper");

	// fall down to y=-64 and below
	public static final LiteralRemovalReason VOID = new LiteralRemovalReason("void");

	// for 1.16+
	public static final LiteralRemovalReason ON_VEHICLE = new LiteralRemovalReason("on_vehicle", "Rides on a vehicle");

	private final String translationKey;

	private LiteralRemovalReason(String translationKey)
	{
		this.translationKey = translationKey;
	}

	@Override
	public BaseText toText()
	{
		return tr(this.translationKey);
	}
}
