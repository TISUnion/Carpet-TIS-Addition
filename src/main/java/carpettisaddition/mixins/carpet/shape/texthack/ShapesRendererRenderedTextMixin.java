package carpettisaddition.mixins.carpet.shape.texthack;

import carpet.script.utils.ShapeDispatcher;
import carpet.script.utils.ShapesRenderer;
import carpettisaddition.helpers.carpet.shape.IShapeDispatcherText;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("SimplifiableConditionalExpression")
@Mixin(ShapesRenderer.RenderedText.class)
public abstract class ShapesRendererRenderedTextMixin<T> extends ShapesRenderer.RenderedShape<ShapeDispatcher.Text>
{
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
							target = "Lcarpet/script/utils/ShapeDispatcher$Text;indent:F"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;scalef(FFF)V",
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
					target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/client/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I",
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
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;popMatrix()V",
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
}
