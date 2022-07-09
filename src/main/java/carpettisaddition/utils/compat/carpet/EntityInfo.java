package carpettisaddition.utils.compat.carpet;

import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.world.World;

import java.util.List;

/**
 * Fabric carpet removed EntityInfo in mc1.17+ and here's a fake one
 * Used in {@link carpettisaddition.mixins.command.info.entity.EntityInfoMixin}
 */
public class EntityInfo
{
	public static List<BaseText> entityInfo(Entity entity, World world)
	{
		return null;
	}
}
