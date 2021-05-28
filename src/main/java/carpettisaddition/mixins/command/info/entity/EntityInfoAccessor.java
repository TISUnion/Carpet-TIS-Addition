package carpettisaddition.mixins.command.info.entity;

import carpet.utils.EntityInfo;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityInfo.class)
public interface EntityInfoAccessor
{
	@Invoker(value = "makeTime", remap = false)
	static String makeTime(long ticks)
	{
		return null;
	}

	@Invoker(value = "display_item", remap = false)
	static String display_item(ItemStack item)
	{
		return null;
	}

	@Invoker(value = "entity_short_string", remap = false)
	static String entity_short_string(Entity e)
	{
		return null;
	}

	@Invoker(value = "get_speed", remap = false)
	static double get_speed(double internal)
	{
		return 0;
	}

	@Invoker(value = "get_horse_speed_percent", remap = false)
	static double get_horse_speed_percent(double internal)
	{
		return 0;
	}

	@Invoker(value = "get_horse_jump", remap = false)
	static double get_horse_jump(double x)
	{
		return 0;
	}

	@Invoker(value = "get_horse_jump_percent", remap = false)
	static double get_horse_jump_percent(double internal)
	{
		return 0;
	}
}
