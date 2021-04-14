package carpettisaddition.logging.loggers.microtiming.marker;

public enum MicroTimingMarkerType
{
	/**
	 * Dont log block update
	 */
	REGULAR(2.5F),
	/**
	 * Log everything
	 */
	END_ROD(7.0F);

	public final float lineWidth;

	MicroTimingMarkerType(float lineWidth)
	{
		this.lineWidth = lineWidth;
	}

	public float getLineWidth()
	{
		return this.lineWidth;
	}
}
