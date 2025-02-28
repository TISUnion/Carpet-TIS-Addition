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

package carpettisaddition.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReflectionUtils
{
	public static Optional<Object> getField(Object object, String fieldName)
	{
		try
		{
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return Optional.ofNullable(field.get(object));
		}
		catch (NoSuchFieldException | IllegalAccessException e)
		{
			return Optional.empty();
		}
	}

	public static class InvocationException extends RuntimeException
	{
		public InvocationException(Throwable cause)
		{
			super(cause);
		}
	}

	public static Optional<Class<?>> getClass(String className)
	{
		try
		{
			return Optional.of(Class.forName(className));
		}
		catch (ClassNotFoundException e)
		{
			return Optional.empty();
		}
	}

	public static BiFunction<Object, Object[], Object> invoker(Method method)
	{
		method.setAccessible(true);
		return (obj, args) -> {
			try
			{
				return method.invoke(obj, args);
			}
			catch (InvocationTargetException | IllegalAccessException e)
			{
				throw new InvocationException(e);
			}
		};
	}

	public static Optional<BiFunction<Object, Object[], Object>> invoker(Class<?> clazz, String methodName, Predicate<Method> methodPredicate)
	{
		for (Method method : clazz.getDeclaredMethods())
		{
			if (method.getName().equals(methodName) && methodPredicate.test(method))
			{
				return Optional.of(invoker(method));
			}
		}
		return Optional.empty();
	}

	public static Optional<BiFunction<Object, Object[], Object>> invoker(String className, String methodName, Predicate<Method> methodPredicate)
	{
		return getClass(className).flatMap(clazz -> invoker(clazz, methodName, methodPredicate));
	}

	public static Optional<BiFunction<Object, Object[], Object>> invoker(String className, String methodName)
	{
		return invoker(className, methodName, m -> true);
	}

	public static Optional<Function<Object[], Object>> constructor(String className, Class<?> ...parameterTypes)
	{
		return getClass(className).map(clazz -> {
			Constructor<?> constructor;
			try
			{
				constructor = clazz.getDeclaredConstructor(parameterTypes);
				constructor.setAccessible(true);
			}
			catch (NoSuchMethodException e)
			{
				return null;
			}
			return args -> {
				try
				{
					return constructor.newInstance(args);
				}
				catch (InvocationTargetException | InstantiationException | IllegalAccessException e)
				{
					return new InvocationException(e);
				}
			};
		});
	}
}
