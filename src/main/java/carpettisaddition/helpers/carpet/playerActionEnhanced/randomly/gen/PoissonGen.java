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

package carpettisaddition.helpers.carpet.playerActionEnhanced.randomly.gen;

public class PoissonGen extends RandomGen
{
	private final double offset;
	private final double lambda;

	public PoissonGen(double offset, double lambda)
	{
		if (lambda < 0)
		{
			throw new RuntimeException("lambda < 0");
		}
		this.offset = offset;
		this.lambda = lambda;
	}

	// https://stackoverflow.com/a/1241605
	// O(lambda)
	private int getPoisson(double lambda)
	{
		double L = Math.exp(-lambda);
		double p = 1.0;
		int k = 0;

		do
		{
			k++;
			p *= this.random.nextDouble();
		}
		while (p > L);

		return k - 1;
	}

	@Override
	protected double generate()
	{
		return this.offset + this.getPoisson(this.lambda);
	}
}
