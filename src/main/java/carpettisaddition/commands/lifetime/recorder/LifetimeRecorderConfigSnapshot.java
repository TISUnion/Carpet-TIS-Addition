/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.recorder;

public class LifetimeRecorderConfigSnapshot
{
	public final boolean enabled;
	public final int requiredPermissionLevel;
	public final boolean consoleOrSinglePlayerOwnerOnly;
	public final String outputDirectory;
	public final long maxOutputRecordCount;
	public final long maxOutputFileBytes;
	public final long maxTotalOutputFileCount;
	public final long maxTotalOutputFileBytes;
	public final double sampleRate;

	public LifetimeRecorderConfigSnapshot(LifetimeRecorderConfig config)
	{
		this.enabled = config.enabled;
		this.requiredPermissionLevel = config.requiredPermissionLevel;
		this.consoleOrSinglePlayerOwnerOnly = config.consoleOrSinglePlayerOwnerOnly;
		this.outputDirectory = config.outputDirectory;
		this.maxOutputRecordCount = config.maxOutputRecordCount;
		this.maxOutputFileBytes = config.maxOutputFileBytes;
		this.maxTotalOutputFileCount = config.maxTotalOutputFileCount;
		this.maxTotalOutputFileBytes = config.maxTotalOutputFileBytes;
		this.sampleRate = config.sampleRate;
	}
}
