package carpettisaddition.commands.lifetime.spawning;

import net.minecraft.text.BaseText;

public class LiteralSpawningReason extends SpawningReason
{
	public static final LiteralSpawningReason NATURAL = new LiteralSpawningReason("natural");
	public static final LiteralSpawningReason PORTAL_PIGMAN = new LiteralSpawningReason("portal_pigman");
	public static final LiteralSpawningReason COMMAND = new LiteralSpawningReason("command");
	public static final LiteralSpawningReason ITEM = new LiteralSpawningReason("item");
	public static final LiteralSpawningReason BLOCK_DROP = new LiteralSpawningReason("block_drop");
	public static final LiteralSpawningReason SLIME = new LiteralSpawningReason("slime");
	public static final LiteralSpawningReason ZOMBIE_REINFORCE = new LiteralSpawningReason("zombie_reinforce");
	public static final LiteralSpawningReason SPAWNER = new LiteralSpawningReason("spawner");
	public static final LiteralSpawningReason RAID = new LiteralSpawningReason("raid");
	public static final LiteralSpawningReason SUMMON = new LiteralSpawningReason("summon");
	public static final LiteralSpawningReason BREEDING = new LiteralSpawningReason("breeding");

	private final String translationKey;

	private LiteralSpawningReason(String translationKey)
	{
		this.translationKey = translationKey;
	}

	@Override
	public BaseText toText()
	{
		return tr(this.translationKey);
	}
}
