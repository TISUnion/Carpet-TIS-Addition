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

package carpettisaddition.commands.speedtest;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.speedtest.ping.PongReceiver;
import carpettisaddition.commands.speedtest.session.SpeedTestClientSessionHolder;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSession;
import carpettisaddition.commands.speedtest.session.SpeedTestServerSessionHolder;
import carpettisaddition.commands.speedtest.tester.*;
import carpettisaddition.network.HandlerContext;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SpeedTestCommand extends AbstractCommand
{
	public static final String NAME = "speedtest";
	private static final SpeedTestCommand INSTANCE = new SpeedTestCommand();

	private final SpeedTestClientSessionHolder clientSessionHolder = new SpeedTestClientSessionHolder();  // used in client only
	private final SpeedTestServerSessionHolder serverSessionHolder = new SpeedTestServerSessionHolder();  // used in server only

	public SpeedTestCommand()
	{
		super(NAME);
	}

	public static SpeedTestCommand getInstance()
	{
		return INSTANCE;
	}

	// ============================ lifecycle ============================

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		context.dispatcher.register(
				literal(NAME).
						requires(s -> CarpetModUtil.canUseCommand(s, CarpetTISAdditionSettings.commandSpeedTest)).
						executes(c -> showHelp(c.getSource())).
						then(literal("download").
								executes(c -> testDownload(c.getSource(), Math.min(10, CarpetTISAdditionSettings.speedTestCommandMaxTestSize))).
								then(argument("size_mib", integer(1)).
										executes(c -> testDownload(c.getSource(), getInteger(c, "size_mib")))
								)
						).
						then(literal("upload").
								executes(c -> testUpload(c.getSource(), Math.min(10, CarpetTISAdditionSettings.speedTestCommandMaxTestSize))).
								then(argument("size_mib", integer(1)).
										executes(c -> testUpload(c.getSource(), getInteger(c, "size_mib")))
								)
						).
						then(literal("ping").
								executes(c -> testPing(c.getSource(), 3, 1)).
								then(argument("count", integer(1)).
										executes(c -> testPing(c.getSource(), getInteger(c, "count"), 1)).
										then(argument("interval", doubleArg(0, 10)).
												executes(c -> testPing(c.getSource(), getInteger(c, "count"), getDouble(c, "interval")))
										)
								)
						).
						then(literal("abort").
								executes(c -> abortTest(c.getSource()))
						)
		);
	}

	public void onServerClosed()
	{
		this.clientSessionHolder.reset();
		this.serverSessionHolder.reset();
	}

	public void onPlayerDisconnected(ServerPlayerEntity player)
	{
		this.serverSessionHolder.abortAndClearFor(player);
	}

	// ============================ commands ============================

	private int showHelp(ServerCommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();

		List<TISCMProtocol.S2C> idToCheck = Lists.newArrayList(
				TISCMProtocol.S2C.SPEED_TEST_DOWNLOAD_PAYLOAD,
				TISCMProtocol.S2C.SPEED_TEST_UPLOAD_REQUEST,
				TISCMProtocol.S2C.SPEED_TEST_PING,
				TISCMProtocol.S2C.SPEED_TEST_ABORT
		);

		int cnt = idToCheck.stream().mapToInt(id -> {
			boolean ok = TISCMServerPacketHandler.getInstance().doesClientSupport(player.networkHandler, id);
			return ok ? 1 : 0;
		}).sum();

		BaseText supportState;
		if (cnt == 0)
		{
			supportState = Messenger.formatting(tr("help.support_state.no"), Formatting.RED);
		}
		else if (cnt < idToCheck.size())
		{
			supportState = Messenger.formatting(tr("help.support_state.partial"), Formatting.YELLOW);
		}
		else
		{
			supportState = Messenger.formatting(tr("help.support_state.yes"), Formatting.GREEN);
		}

		Messenger.tell(player, tr(
				"help",
				Messenger.hover(
						Messenger.s(String.valueOf(CarpetTISAdditionSettings.speedTestCommandMaxTestSize)),
						tr("help.test_size_rule_hint")
				),
				supportState
		));
		return 0;
	}

	private int abortTest(ServerCommandSource source) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();

		SpeedTestServerSession session = this.serverSessionHolder.getFor(player);
		if (session == null)
		{
			Messenger.tell(source, tr("command.abort_no_test"));
			return 0;
		}

		session.getMessenger().sendMessage(tr("command.abort_test"), false);
		this.serverSessionHolder.abortAndClearFor(player);

		return 1;
	}

	private int testDownload(ServerCommandSource source, int testSizeMb) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();

		if (!checkSpeedTestRequirements(player, testSizeMb, TISCMProtocol.S2C.SPEED_TEST_DOWNLOAD_PAYLOAD))
		{
			return 0;
		}

		new SpeedTestDownloader(
				player, testSizeMb, this.serverSessionHolder,
				new SpeedTestReporter(TestType.DOWNLOAD, source)
		).start();

		return 1;
	}

	private int testUpload(ServerCommandSource source, int testSizeMb) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();

		if (!checkSpeedTestRequirements(player, testSizeMb, TISCMProtocol.S2C.SPEED_TEST_UPLOAD_REQUEST))
		{
			return 0;
		}

		new SpeedTestServerUploadReceiver(
				player, testSizeMb, this.serverSessionHolder,
				new SpeedTestReporter(TestType.UPLOAD, source)
		).start();

		return 1;
	}

	private int testPing(ServerCommandSource source, int count, double interval) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();

		if (!checkSpeedTestRequirements(player, null, TISCMProtocol.S2C.SPEED_TEST_PING))
		{
			return 0;
		}

		new SpeedTestPinger(source, player, count, interval, this.serverSessionHolder).start();

		return 1;
	}

	// ============================ utils ============================

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean checkSpeedTestRequirements(ServerPlayerEntity player, @Nullable Integer testSizeMb, TISCMProtocol.S2C ...packetIds)
	{
		if (testSizeMb != null && testSizeMb > CarpetTISAdditionSettings.speedTestCommandMaxTestSize)
		{
			Messenger.tell(player, tr("command.test_size_too_large", testSizeMb, CarpetTISAdditionSettings.speedTestCommandMaxTestSize));
			return false;
		}

		if (!CarpetTISAdditionSettings.tiscmNetworkProtocol)
		{
			Messenger.tell(player, tr("command.tiscm_protocol_disabled"));
			return false;
		}

		for (TISCMProtocol.S2C packetId : packetIds)
		{
			if (!TISCMServerPacketHandler.getInstance().doesClientSupport(player.networkHandler, packetId))
			{
				Messenger.tell(player, tr("command.client_not_supported"));
				return false;
			}
		}

		if (this.serverSessionHolder.getFor(player) != null)
		{
			Messenger.tell(player, tr("command.has_ongoing"));
			return false;
		}

		return true;
	}

	// ============================ network ============================

	public void handleServerUploadRequest(HandlerContext.S2C ctx)
	{
		int testSizeMb = ctx.payload.getInt("size_mb");
		if (this.clientSessionHolder.getUploader() != null)
		{
			CarpetTISAdditionMod.LOGGER.error("Received upload speed test request, but there's ongoing test. Overriding");
		}

		CarpetTISAdditionMod.LOGGER.info("Starting upload speed test, test size = {}MiB", testSizeMb);
		new SpeedTestClientUploader(testSizeMb, this.clientSessionHolder).start();
	}

	public void handleServerPing(HandlerContext.S2C ctx)
	{
		String pingType = ctx.payload.getString("type");
		switch (pingType)
		{
			case "ping":
				TISCMClientPacketHandler.getInstance().sendPacket(TISCMProtocol.C2S.SPEED_TEST_PING, nbt -> {
					nbt.copyFrom(ctx.payload);
					nbt.putString("type", "pong");
				});
				break;
			case "pong":
				SpeedTester tester = this.clientSessionHolder.getUploader();
				if (tester instanceof PongReceiver)
				{
					((PongReceiver)tester).onPongReceived(ctx.payload);
				}
				break;
			default:
				CarpetTISAdditionMod.LOGGER.warn("Received unknown ping type '{}'", pingType);
		}
	}

	public void handleServerTestAbort(HandlerContext.S2C ctx)
	{
		this.clientSessionHolder.abortAndClear();
	}

	public void handleClientSpeedTestUploadPayload(HandlerContext.C2S ctx)
	{
		SpeedTester tester = this.serverSessionHolder.getFor(ctx.player);
		if (tester instanceof SpeedTestServerUploadReceiver)
		{
			((SpeedTestServerUploadReceiver)tester).onC2SPayloadPacketReceived();
		}
	}

	public void handleClientPing(HandlerContext.C2S ctx)
	{
		String pingType = ctx.payload.getString("type");
		switch (pingType)
		{
			case "ping":
				TISCMServerPacketHandler.getInstance().sendPacket(ctx.networkHandler, TISCMProtocol.S2C.SPEED_TEST_PING, nbt -> {
					nbt.copyFrom(ctx.payload);
					nbt.putString("type", "pong");
				});
				break;
			case "pong":
				SpeedTester tester = this.serverSessionHolder.getFor(ctx.player);
				if (tester instanceof PongReceiver)
				{
					((PongReceiver)tester).onPongReceived(ctx.payload);
				}
				break;
			default:
				CarpetTISAdditionMod.LOGGER.warn("Received unknown ping type '{}'", pingType);
		}
	}
}
