package carpettisaddition.logging.loggers.damage.modifyreasons;

public class Modification
{
	private final ModifyReason reason;
	private final float oldAmount;
	private final float newAmount;

	public Modification(float oldAmount, float newAmount, ModifyReason reason)
	{
		this.reason = reason;
		this.oldAmount = oldAmount;
		this.newAmount = newAmount;
	}

	public ModifyReason getReason()
	{
		return reason;
	}

	public float getOldAmount()
	{
		return oldAmount;
	}

	public float getNewAmount()
	{
		return newAmount;
	}
}
