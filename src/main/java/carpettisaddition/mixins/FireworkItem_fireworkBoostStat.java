package carpettisaddition.mixins;

import carpettisaddition.helpers.CustomStatsHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(FireworkItem.class)
public class FireworkItem_fireworkBoostStat
{
	@Inject(
			method = "use",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
			)
	)
	private void onFireworkBoost(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci)
	{
		user.increaseStat(CustomStatsHelper.FIREWORK_BOOST, 1);
	}
}
