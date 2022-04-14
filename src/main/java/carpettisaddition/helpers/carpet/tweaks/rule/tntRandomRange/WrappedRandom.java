package carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.utils.GameUtil;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.util.math.random.RandomDeriver;

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
public class WrappedRandom implements AbstractRandom
{
	private final AbstractRandom random;

	private WrappedRandom(AbstractRandom random)
	{
		this.random = random;
	}

	public static WrappedRandom wrap(AbstractRandom random)
	{
		return new WrappedRandom(random);
	}

	public AbstractRandom unwrap()
	{
		return this.random;
	}

	@Override
	public AbstractRandom derive()
	{
		return this.random.derive();
	}

	@Override
	public RandomDeriver createRandomDeriver()
	{
		return this.random.createRandomDeriver();
	}

	@Override
	public void setSeed(long seed)
	{
		this.random.setSeed(seed);
	}

	@Override
	public int nextInt()
	{
		return this.random.nextInt();
	}

	@Override
	public int nextInt(int bound)
	{
		return this.random.nextInt(bound);
	}

	@Override
	public long nextLong()
	{
		return this.random.nextLong();
	}

	@Override
	public boolean nextBoolean()
	{
		return this.random.nextBoolean();
	}

	@Override
	public float nextFloat()
	{
		float vanillaResult = this.random.nextFloat();  // to make sure world random gets triggered too
		return CarpetSettings.tntRandomRange >= 0 && GameUtil.isOnServerThread() ? (float)CarpetSettings.tntRandomRange : vanillaResult;
	}

	@Override
	public double nextDouble()
	{
		return this.random.nextDouble();
	}

	@Override
	public double nextGaussian()
	{
		return this.random.nextGaussian();
	}
}
