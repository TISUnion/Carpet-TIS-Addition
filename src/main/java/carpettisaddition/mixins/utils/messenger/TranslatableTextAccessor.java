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

import net.minecraft.text.TranslatableText;
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
//$$ import net.minecraft.text.StringVisitable;
//#else
import net.minecraft.text.Text;
//#endif

@Mixin(TranslatableText.class)
public interface TranslatableTextAccessor
{
	@Accessor
	@Mutable
	void setArgs(Object[] args);

	//#if MC < 11800

	@Accessor
	//#if MC >= 11600
	//$$ List<StringVisitable> getTranslations();
	//#else
	List<Text> getTranslations();
	//#endif

	//#endif


	@Invoker
	//#if MC >= 11800
	//$$ void invokeForEachPart(String translation, Consumer<StringVisitable> partsConsumer);
	//#else
	void invokeSetTranslation(String translation);
	//#endif
}
