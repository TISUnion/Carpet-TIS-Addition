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

package carpettisaddition.network;

import net.minecraft.util.Identifier;

//#if MC >= 12002
//$$ import net.minecraft.network.packet.BrandCustomPayload;
//#else
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
//#endif

public class TISCMNetworkUtils
{
	// a ctx handler that does nothing
	public static void blackHole(HandlerContext.S2C ctx)
	{
	}

	// a ctx handler that does nothing
	public static void blackHole(HandlerContext.C2S ctx)
	{
	}

	public static Identifier getServerBrandCustomPayloadId()
	{
		//#if MC >= 12002
		//$$ return BrandCustomPayload.ID;
		//#else
		return CustomPayloadS2CPacket.BRAND;
		//#endif
	}
}
