package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.translations.TranslationContext;
import net.minecraft.text.MutableText;

public class ModifyReason extends TranslationContext
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

	protected ModifyReason(String name)
	{
		super(DamageLogger.getInstance().getTranslator());
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public MutableText toText()
	{
		return tr("modify_reason." + this.getName().toLowerCase().replace(" ", "_"));
	}
}
