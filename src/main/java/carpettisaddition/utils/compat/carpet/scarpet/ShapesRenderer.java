package carpettisaddition.utils.compat.carpet.scarpet;

public class ShapesRenderer
{
	public abstract static class RenderedShape<T extends ShapeDispatcher.ExpiringShape>
	{
	}

	public static class RenderedText extends RenderedShape<ShapeDispatcher.Text>
	{
	}
}
