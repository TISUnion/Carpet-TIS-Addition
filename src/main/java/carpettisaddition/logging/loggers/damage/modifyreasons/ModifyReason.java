package carpettisaddition.logging.loggers.damage.modifyreasons;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.translations.TranslationContext;
import net.minecraft.text.BaseText;

public class ModifyReason extends TranslationContext
{
	public static final ModifyReason HELMET = new ModifyReason("wearing_a_helmet");
	public static final ModifyReason SHIELD = new ModifyReason("holding_a_shield");
	public static final ModifyReason RECENTLY_HIT = new ModifyReason("recently_hit");
	public static final ModifyReason DIFFICULTY = new ModifyReason("difficulty");
	public static final ModifyReason IMMUNE = new ModifyReason("immuse_to_damage");
	public static final ModifyReason INVULNERABLE = new ModifyReason("invulnerable");
	public static final ModifyReason RESPAWN_PROTECTION = new ModifyReason("respawn_protection");
	public static final ModifyReason PVP_DISABLED = new ModifyReason("pvp_disabled");

	private final String translationName;

	protected ModifyReason(String translationName)
	{
		super(DamageLogger.getInstance().getTranslator());
		this.translationName = translationName;
	}

	public BaseText toText()
	{
		return tr("modify_reason." + this.translationName);
	}
}
