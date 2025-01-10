/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.utils;

public class NanoTimer
{
	private final long createdNs;
	private long startNs;

	public NanoTimer()
	{
		this.createdNs = getNowNs();
		this.start();
	}

	private static long getNowNs()
	{
		return System.nanoTime();
	}

	public void start()
	{
		this.startNs = getNowNs();
	}

	public void restart()
	{
		this.start();
	}

	public long getElapsedNs()
	{
		return getNowNs() - this.startNs;
	}

	public double getElapsedSec()
	{
		return this.getElapsedNs() / 1e9;
	}

	public long getElapsedNsRestart()
	{
		long elapsed = getElapsedNs();
		this.restart();
		return elapsed;
	}

	public double getElapsedSecRestart()
	{
		double elapsed = getElapsedSec();
		this.restart();
		return elapsed;
	}

	public long getTotalElapsedNs()
	{
		return getNowNs() - this.createdNs;
	}

	public double getTotalElapsedSec()
	{
		return this.getTotalElapsedNs() / 1e9;
	}
}
