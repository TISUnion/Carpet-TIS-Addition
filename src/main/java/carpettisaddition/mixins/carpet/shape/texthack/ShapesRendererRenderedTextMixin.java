package carpettisaddition.mixins.carpet.shape.texthack;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.utils.ShapesRenderer;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11500
import carpettisaddition.helpers.carpet.shape.IShapeDispatcherText;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

//#if MC >= 11500
@SuppressWarnings("SimplifiableConditionalExpression")
//#endif
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.15"))
@Mixin(ShapesRenderer.RenderedText.class)
public abstract class ShapesRendererRenderedTextMixin<T> extends ShapesRenderer.RenderedShape<ShapeDispatcher.Text>
{
	//#if MC >= 11500
	protected ShapesRendererRenderedTextMixin(MinecraftClient client, ShapeDispatcher.Text shape)
	{
		super(client, shape);
	}

	private boolean isMicroTimingMarkerText()
	{
		return ((IShapeDispatcherText)this.shape).isMicroTimingMarkerText();
	}

	@Inject(
			method = "renderLines",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							//#if MC >= 11600
							//$$ target = "Lcarpet/script/utils/ShapeDispatcher$DisplayedText;indent:F"
							//#else
							target = "Lcarpet/script/utils/ShapeDispatcher$Text;indent:F"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V",
					//#else
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;scalef(FFF)V",
					//#endif
					ordinal = 0,
					remap = true
			),
			remap = false
	)
	private void doSomeTransparentThing(CallbackInfo ci)
	{
		if (this.isMicroTimingMarkerText())
		{
			RenderSystem.disableDepthTest();
		}
	}

	@ModifyArg(
			method = "renderLines",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
					//#else
					target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
					//#endif
					remap = true
			),
			index = 7,
			remap = false
	)
	private boolean seeThroughWhenNecessary(boolean value)
	{
		return this.isMicroTimingMarkerText() ? true : value;
	}


	@Inject(
			method = "renderLines",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V",
					//#else
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;popMatrix()V",
					//#endif
					ordinal = 0,
					remap = true
			),
			remap = false
	)
	private void doSomeCleanUpForTransparentThing(CallbackInfo ci)
	{
		if (this.isMicroTimingMarkerText())
		{
			RenderSystem.enableDepthTest();
		}
	}
	//#endif
}
