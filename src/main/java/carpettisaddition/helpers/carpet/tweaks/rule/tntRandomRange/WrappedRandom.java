/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.helpers.carpet.tweaks.rule.tntRandomRange;

import carpet.CarpetSettings;
import carpettisaddition.utils.GameUtils;

// don't do the "net.minecraft.util.math.random.Random <-> net.minecraft.world.gen.random.AbstractRandom" remap thing
//#disable-remap

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
		return CarpetSettings.tntRandomRange >= 0 && GameUtils.isOnServerThread() ? (float)CarpetSettings.tntRandomRange : vanillaResult;
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

//#enable-remap
