package carpettisaddition.logging.loggers.microtiming.marker;

import net.minecraft.util.Formatting;

public enum MicroTimingMarkerType
{
	/**
	 * Dont log block update
	 */
	REGULAR(2.5F, Formatting.GRAY),
	/**
	 * Log everything
	 */
	END_ROD(7.0F, Formatting.LIGHT_PURPLE);

	public final float lineWidth;
	private final Formatting color;

	MicroTimingMarkerType(float lineWidth, Formatting color)
	{
		this.lineWidth = lineWidth;
		this.color = color;
	}

	public float getLineWidth()
	{
		return this.lineWidth;
	}

	public String getFancyString()
	{
		return this.color.toString() + super.toString() + Formatting.RESET;
	}
}
