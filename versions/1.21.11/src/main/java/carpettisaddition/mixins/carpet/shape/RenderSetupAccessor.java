/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.carpet.shape;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.TextureTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

/**
 *            mc <  1.21.11: subproject 1.15.2 (main project)
 * 1.21.11 <= mc <  26.2   : subproject 1.21.11                <--------
 *            mc >= 26.2   : subproject 26.2
 */
@Mixin(RenderSetup.class)
public interface RenderSetupAccessor
{
	@Accessor("layeringTransform")
	LayeringTransform getLayeringTransform$TISCM();

	@Accessor("outlineProperty")
	RenderSetup.OutlineProperty getOutlineProperty$TISCM();

	@Accessor("outputTarget")
	OutputTarget getOutputTarget$TISCM();

	@Accessor("textures")
	Map<String, RenderSetup.TextureBinding> getTextures$TISCM();

	@Accessor("textureTransform")
	TextureTransform getTextureTransform$TISCM();

	@Accessor("useLightmap")
	boolean getUseLightmap$TISCM();

	@Accessor("useOverlay")
	boolean getUseOverlay$TISCM();

	@Invoker("<init>")
	static RenderSetup invokeConstructor(
			RenderPipeline pipeline, Map<String, RenderSetup.TextureBinding> textures, boolean useLightmap, boolean useOverlay, LayeringTransform layeringTransform,
			OutputTarget outputTarget, TextureTransform textureTransform, RenderSetup.OutlineProperty outlineProperty, boolean affectsCrumbling, boolean sortOnUpload, int bufferSize
	)
	{
		throw new AssertionError();
	}
}
