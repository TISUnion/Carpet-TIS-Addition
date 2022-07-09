package carpettisaddition.utils.compat.carpet.scarpet;

import carpet.script.value.Value;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Map;

//#if MC < 11700
import org.apache.commons.lang3.tuple.Pair;
//#endif

public class ShapeDispatcher
{
	//#if MC >= 11700
	//$$ public static class ShapeWithConfig
	//$$ {
	//$$ }
	//#endif

	public static void sendShape(
			List<ServerPlayerEntity> subscribedPlayers,
			//#if MC >= 11700
			//$$ List<ShapeWithConfig> shapeDataList
			//#else
			List<Pair<ExpiringShape, Map<String, Value>>> shapeDataList
			//#endif
	)
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

	public static class FormattedTextParam
	{
	}
}