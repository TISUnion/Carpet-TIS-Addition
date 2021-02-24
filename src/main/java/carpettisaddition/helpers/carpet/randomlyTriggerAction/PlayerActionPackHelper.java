package carpettisaddition.helpers.carpet.randomlyTriggerAction;

import carpet.helpers.EntityPlayerActionPack;

import java.util.Random;

public class PlayerActionPackHelper
{
	private static final Random random = new Random();

	public static int getRandomInt(int lower, int upper)
	{
		return lower + random.nextInt(upper - lower + 1);
	}

	public static EntityPlayerActionPack.Action getRandomlyTriggerAction(int lower, int upper)
	{
		upper = Math.max(lower, upper);
		EntityPlayerActionPack.Action action = EntityPlayerActionPack.Action.interval(getRandomInt(lower, upper));
		((IEntityPlayerActionPackAction) action).setRandomlyRange(lower, upper);
		return action;
	}
}
