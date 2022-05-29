package carpettisaddition.logging.loggers.movement;

import carpettisaddition.translations.TranslationContext;
import net.minecraft.text.BaseText;

public class MovementModification extends TranslationContext
{
	public static final MovementModification PISTON = new MovementModification("piston");
	public static final MovementModification SNEAKING = new MovementModification("sneaking");
	public static final MovementModification COLLISION = new MovementModification("collision");

	private final String translationName;

	protected MovementModification(String translationName)
	{
		super(MovementLogger.getInstance().getTranslator().getDerivedTranslator("modification"));
		this.translationName = translationName;
	}

	public BaseText toText()
	{
		return tr(this.translationName);
	}
}
