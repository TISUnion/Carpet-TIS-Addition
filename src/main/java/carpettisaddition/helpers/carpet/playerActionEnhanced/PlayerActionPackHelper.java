package carpettisaddition.helpers.carpet.playerActionEnhanced;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.mixins.carpet.tweaks.command.playerActionEnhanced.EntityPlayerActionPackActionAccessor;

import java.util.Random;

public class PlayerActionPackHelper
{
	private static final Random random = new Random();

	public static int getRandomInt(int lower, int upper)
	{
		return lower + random.nextInt(upper - lower + 1);
	}

	public static EntityPlayerActionPack.Action randomly(int lower, int upper)
	{
		upper = Math.max(lower, upper);
		EntityPlayerActionPack.Action action = EntityPlayerActionPack.Action.interval(getRandomInt(lower, upper));
		((IEntityPlayerActionPackAction) action).setRandomlyRange(lower, upper);
		return action;
	}
	public static EntityPlayerActionPack.Action after(int delay)
	{
		return EntityPlayerActionPackActionAccessor.invokeConstructor(1, 1, delay);
	}
}
