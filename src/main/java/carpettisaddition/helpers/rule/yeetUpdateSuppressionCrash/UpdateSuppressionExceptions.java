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

package carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class UpdateSuppressionExceptions
{
	@Nullable
	public static UpdateSuppressionException createWrapper(Throwable cause, @Nullable World world, BlockPos pos)
	{
		if (cause instanceof ClassCastException)
		{
			return new ClassCastSuppression(cause, world, pos);
		}
		if (cause instanceof IllegalArgumentException)
		{
			return new IllegalArgumentSuppression(cause, world, pos);
		}

		// StackOverflowError or OutOfMemoryError could get wrapped when Minecraft is handling it after being firstly thrown
		for (; cause != null; cause = cause.getCause())
		{
			if (cause instanceof StackOverflowError)
			{
				return new StackOverflowSuppression(cause, world, pos);
			}
			if (cause instanceof OutOfMemoryError)
			{
				return new OutOfMemorySuppression(cause, world, pos);
			}
		}
		return null;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static void noop()
	{
		UpdateSuppressionException.class.getClass();
		StackOverflowSuppression.class.getClass();
		ClassCastSuppression.class.getClass();
		OutOfMemorySuppression.class.getClass();
		IllegalArgumentSuppression.class.getClass();
	}

	public static class StackOverflowSuppression extends StackOverflowError implements UpdateSuppressionException
	{
		private final UpdateSuppressionContext context;

		public StackOverflowSuppression(Throwable cause, @Nullable World world, BlockPos pos)
		{
			this.context = new UpdateSuppressionContext(cause, world, pos);
		}

		@Override
		public synchronized Throwable getCause()
		{
			return this.context.getCause();
		}

		@Override
		public UpdateSuppressionContext getSuppressionContext()
		{
			return this.context;
		}
	}

	public static class ClassCastSuppression extends ClassCastException implements UpdateSuppressionException
	{
		private final UpdateSuppressionContext context;

		public ClassCastSuppression(Throwable cause, @Nullable World world, BlockPos pos)
		{
			this.context = new UpdateSuppressionContext(cause, world, pos);
		}

		@Override
		public synchronized Throwable getCause()
		{
			return this.context.getCause();
		}

		@Override
		public UpdateSuppressionContext getSuppressionContext()
		{
			return this.context;
		}
	}

	public static class OutOfMemorySuppression extends OutOfMemoryError implements UpdateSuppressionException
	{
		private final UpdateSuppressionContext context;

		public OutOfMemorySuppression(Throwable cause, @Nullable World world, BlockPos pos)
		{
			this.context = new UpdateSuppressionContext(cause, world, pos);
		}

		@Override
		public synchronized Throwable getCause()
		{
			return this.context.getCause();
		}

		@Override
		public UpdateSuppressionContext getSuppressionContext()
		{
			return this.context;
		}
	}

	public static class IllegalArgumentSuppression extends IllegalArgumentException implements UpdateSuppressionException
	{
		private final UpdateSuppressionContext context;

		public IllegalArgumentSuppression(Throwable cause, @Nullable World world, BlockPos pos)
		{
			super(cause);
			this.context = new UpdateSuppressionContext(cause, world, pos);
		}

		@Override
		public synchronized Throwable getCause()
		{
			return this.context.getCause();
		}

		@Override
		public UpdateSuppressionContext getSuppressionContext()
		{
			return this.context;
		}
	}
}