package carpettisaddition.logging.loggers.microtick.types;

public class PowerState
{
	private final int strength;
	private final boolean rough;

	public PowerState(int strength, boolean rough)
	{
		this.strength = strength;
		this.rough = rough;
	}

	public static PowerState of(int strength)
	{
		return new PowerState(strength, false);
	}

	public static PowerState of(boolean powered)
	{
		return new PowerState(powered ? 15 : 0, true);
	}

	public boolean isPowered()
	{
		return this.strength > 0;
	}

	@Override
	public String toString()
	{
		String str = this.isPowered() ? "Powered" : "Depowered";
		if (!this.rough && this.isPowered())
		{
			str += "(" + this.strength + ")";
		}
		return str;
	}
}
