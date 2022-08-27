package carpettisaddition.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReflectionUtil
{
	private static final Object INVOKE_FAILURE = new Object();

	public static boolean invokeNormally(Object... returnValues)
	{
		for (Object returnValue : returnValues)
		{
			if (returnValue == INVOKE_FAILURE)
			{
				return false;
			}
		}
		return true;
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

	public static Optional<BiFunction<Object, Object[], Object>> invoker(String className, String methodName, Predicate<Method> methodPredicate)
	{
		return getClass(className).map(clazz -> {
			for (Method method : clazz.getDeclaredMethods())
			{
				if (method.getName().equals(methodName) && methodPredicate.test(method))
				{
					method.setAccessible(true);
					return (obj, args) -> {
						try
						{
							return method.invoke(obj, args);
						}
						catch (Exception e)
						{
							return INVOKE_FAILURE;
						}
					};
				}
			}
			return null;
		});
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
				catch (Exception e)
				{
					return INVOKE_FAILURE;
				}
			};
		});
	}
}
