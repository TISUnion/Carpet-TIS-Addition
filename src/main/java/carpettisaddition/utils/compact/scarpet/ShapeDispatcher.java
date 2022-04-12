package carpettisaddition.utils.compact.scarpet;

import carpet.script.value.Value;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ShapeDispatcher
{
	public static void sendShape(List<ServerPlayerEntity> subscribedPlayers, List<Pair<ExpiringShape, Map<String, Value>>> shapeDataList)
	{
	}

	public static class ExpiringShape
	{
		protected void init(Map<String, Value> options)
		{

		}
	}

	public static class Line extends ExpiringShape
	{
		private Line()
		{
		}
	}

	public static class Box extends ExpiringShape
	{
	}

	public static class Sphere extends ExpiringShape
	{
		private Sphere()
		{
		}
	}

	public static class Text extends ExpiringShape
	{
	}
}
