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

public class RangeLimitedGen extends RandomGen
{
	private final RandomGen delegate;
	private final int lowerBound;
	private final int upperBound;

	public RangeLimitedGen(RandomGen delegate, int lowerBound, int upperBound)
	{
		this.delegate = delegate;
		this.lowerBound = lowerBound;
		this.upperBound = Math.max(lowerBound, upperBound);
	}

	@Override
	protected double generate()
	{
		return this.delegate.generate();
	}

	@Override
	protected int generateInt()
	{
		return Math.min(Math.max(this.lowerBound, super.generateInt()), this.upperBound);
	}
}
