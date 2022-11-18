package carpettisaddition.commands.lifetime.removal;

import net.minecraft.text.BaseText;

public class LiteralRemovalReason extends RemovalReason
{
	// 32m ~ 128m randomly despawn
	public static final LiteralRemovalReason DESPAWN_RANDOMLY = regular("despawn.randomly");
	// > 128m immediately despawn
	public static final LiteralRemovalReason DESPAWN_IMMEDIATELY = regular("despawn.immediately");
	// difficulty peaceful
	public static final LiteralRemovalReason DESPAWN_DIFFICULTY = regular("despawn.difficulty");
	// item/xp orb timeout
	public static final LiteralRemovalReason DESPAWN_TIMEOUT = regular("despawn.timeout");

	// when the persistent tag set to true, treat it as removed since it doesn't count towards mobcaps anymore
	public static final LiteralRemovalReason PERSISTENT = yeetedFromCap("persistent");

	// the fallback reason
	public static final LiteralRemovalReason OTHER = regular("other");

	// for item entity and xp orb entity
	public static final LiteralRemovalReason MERGE = regular("merge");

	// for item entity
	public static final LiteralRemovalReason HOPPER = regular("hopper");

	// fall down to y=-64 and below
	public static final LiteralRemovalReason VOID = regular("void");

	// for 1.16+, general
	public static final LiteralRemovalReason ON_VEHICLE = yeetedFromCap("on_vehicle");

	// for 1.16+, enderman
	public static final LiteralRemovalReason PICKUP_BLOCK = yeetedFromCap("pickup_block");

	private final String translationKey;
	private final RemovalType removalType;

	private LiteralRemovalReason(String translationKey, RemovalType removalType)
	{
		this.translationKey = translationKey;
		this.removalType = removalType;
	}
	
	private static LiteralRemovalReason regular(String translationKey)
	{
		return new LiteralRemovalReason(translationKey, RemovalType.REMOVED_FROM_WORLD);
	}
	
	private static LiteralRemovalReason yeetedFromCap(String translationKey)
	{
		return new LiteralRemovalReason(translationKey, RemovalType.REMOVED_FROM_MOBCAP);
	}

	@Override
	public BaseText toText()
	{
		return tr(this.translationKey);
	}

	@Override
	public RemovalType getRemovalType()
	{
		return this.removalType;
	}
}
