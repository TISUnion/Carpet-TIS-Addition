package carpettisaddition.mixins.command.info.entity;

//#if MC >= 11700
//$$ import carpettisaddition.utils.compat.carpet.EntityInfo;
//#else
import carpet.utils.EntityInfo;
//#endif

import carpettisaddition.commands.info.entity.EntityInfoPorting;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(EntityInfo.class)
public abstract class EntityInfoMixin
{
	@Inject(method = "entityInfo", at = @At("HEAD"), cancellable = true, remap = false)
	private static void makeItWorkable(Entity e, World source_world, CallbackInfoReturnable<List<BaseText>> cir)
	{
		cir.setReturnValue(EntityInfoPorting.entityInfo(e, source_world));
	}
}
