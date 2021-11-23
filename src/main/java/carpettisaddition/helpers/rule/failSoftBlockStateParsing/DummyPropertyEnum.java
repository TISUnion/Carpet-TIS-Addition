package carpettisaddition.helpers.rule.failSoftBlockStateParsing;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;

public enum DummyPropertyEnum implements StringIdentifiable
{
	;
	public static final String NAME = "$TCA$DUMMY$";
	public static final EnumProperty<DummyPropertyEnum> DUMMY_PROPERTY = EnumProperty.of(NAME, DummyPropertyEnum.class);

	@Override
	public String asString()
	{
		return NAME;
	}
}
