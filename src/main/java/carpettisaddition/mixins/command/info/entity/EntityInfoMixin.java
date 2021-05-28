package carpettisaddition.mixins.command.info.entity;

import carpet.utils.EntityInfo;
import carpettisaddition.commands.info.entity.EntityInfoPorting;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityInfo.class)
public abstract class EntityInfoMixin
{
	@Inject(method = "entityInfo", at = @At("HEAD"), cancellable = true, remap = false)
	private static void makeItWorkable(Entity e, World source_world, CallbackInfoReturnable<List<BaseText>> cir)
	{
		cir.setReturnValue(EntityInfoPorting.entityInfo(e, source_world));
	}
}
