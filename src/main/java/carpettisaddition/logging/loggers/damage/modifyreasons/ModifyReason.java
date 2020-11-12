package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.damage.DamageLogger;
import net.minecraft.text.BaseText;

public class ModifyReason
{
	public static final ModifyReason HELMET = new ModifyReason("Wearing a helmet");
	public static final ModifyReason SHIELD = new ModifyReason("Holding a shield");
	public static final ModifyReason RECENTLY_HIT = new ModifyReason("Recently hit");
	public static final ModifyReason DIFFICULTY = new ModifyReason("Difficulty");
	public static final ModifyReason IMMUNE = new ModifyReason("Immune to damage");
	public static final ModifyReason INVULNERABLE = new ModifyReason("Invulnerable");
	public static final ModifyReason RESPAWN_PROTECTION = new ModifyReason("Respawn protection");
	public static final ModifyReason PVP_DISABLED = new ModifyReason("PVP disabled");

	private final String name;

	ModifyReason(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public BaseText toText()
	{
		return Messenger.c("w " + this.tr());
	}

	public String tr()
	{
		return DamageLogger.getStaticTranslator().tr("modify_reason." + this.getName(), this.getName(), true);
	}
}
