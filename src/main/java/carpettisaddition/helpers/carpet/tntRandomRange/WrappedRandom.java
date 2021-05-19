package carpettisaddition.helpers.carpet.tntRandomRange;

import carpet.CarpetSettings;

import java.util.Random;

/**
 * A wrapped Random class for controlling the result of nextFloat() during method collectBlocksAndDamageEntities
 * With this the tntRandomRange rule in Carpet works with/without any vanilla explosion optimization mod implementation
 *
 * In vanilla it only invokes this.world.random.nextFloat() in method collectBlocksAndDamageEntities, so it's fine
 * to just override the nextFloat in this class
 *
 * If other mods uses world random inside this method, the result might not be the same with vanilla, but it's
 * already modded so whatever
 */
public class WrappedRandom extends Random
{
	private final Random random;

	private WrappedRandom(Random random)
	{
		this.random = random;
	}

	public static WrappedRandom wrap(Random random)
	{
		return new WrappedRandom(random);
	}

	public Random unwrap()
	{
		return this.random;
	}

	@Override
	public float nextFloat()
	{
		float vanillaResult = this.random.nextFloat();  // to make sure world random gets triggered too
		return CarpetSettings.tntRandomRange >= 0 ? (float)CarpetSettings.tntRandomRange : vanillaResult;
	}
}
