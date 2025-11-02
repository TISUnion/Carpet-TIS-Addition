/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
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

package carpettisaddition.mixins.utils.messenger;

import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC >= 11800
//$$ import java.util.function.Consumer;
//#else
import java.util.List;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.network.chat.FormattedText;
//#else
import net.minecraft.network.chat.Component;
//#endif

@Mixin(TranslatableComponent.class)
public interface TranslatableTextAccessor
{
	@Accessor
	@Mutable
	void setArgs(Object[] args);

	//#if MC < 11800

	@Accessor("decomposedParts")
	//#if MC >= 11600
	//$$ List<FormattedText> getTranslations();
	//#else
	List<Component> getTranslations();
	//#endif

	//#endif


	@Invoker("decomposeTemplate")
	//#if MC >= 11800
	//$$ void invokeForEachPart(String translation, Consumer<FormattedText> partsConsumer);
	//#else
	void invokeSetTranslation(String translation);
	//#endif
}
