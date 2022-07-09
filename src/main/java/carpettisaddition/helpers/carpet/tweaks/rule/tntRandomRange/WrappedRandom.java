package carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.utils.GameUtil;

//#if MC >= 11900
//$$ import net.minecraft.util.math.random.Random;
//$$ import net.minecraft.util.math.random.RandomSplitter;
//#else
import java.util.Random;
//#endif

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
public class WrappedRandom
		//#if MC >= 11900
		//$$ implements
		//#else
		extends
		//#endif
		Random
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
		return CarpetSettings.tntRandomRange >= 0 && GameUtil.isOnServerThread() ? (float)CarpetSettings.tntRandomRange : vanillaResult;
	}

	//#if MC >= 11900
	//$$ @Override public Random split() {return this.random.split();}
	//$$ @Override public RandomSplitter nextSplitter() {return this.random.nextSplitter();}
	//$$ @Override public void setSeed(long seed) {this.random.setSeed(seed);}
	//$$ @Override public int nextInt() {return this.random.nextInt();}
	//$$ @Override public int nextInt(int bound) {return this.random.nextInt(bound);}
	//$$ @Override public long nextLong() {return this.random.nextLong();}
	//$$ @Override public boolean nextBoolean() {return this.random.nextBoolean();}
	//$$ @Override public double nextDouble() {return this.random.nextDouble();}
	//$$ @Override public double nextGaussian() {return this.random.nextGaussian();}
	//#endif
}
