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

public class UniformGen extends RandomGen
{
	private final int lower;
	private final int upper;

	public UniformGen(int lower, int upper)
	{
		this.lower = lower;
		this.upper = Math.max(lower, upper);
	}

	@Override
	protected double generate()
	{
		return this.lower + this.random.nextInt(this.upper - this.lower + 1);
	}
}
