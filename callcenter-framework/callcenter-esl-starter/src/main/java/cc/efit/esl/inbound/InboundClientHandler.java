package cc.efit.esl.inbound;

import cc.efit.esl.core.AbstractEslClientHandler;
import cc.efit.esl.core.Context;
import cc.efit.esl.core.IEslProtocolListener;
import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.message.EslHeaders;
import cc.efit.esl.core.res.CommandResponse;
import io.netty.channel.ChannelHandlerContext;

public class InboundClientHandler extends AbstractEslClientHandler {

	private final String password;
	private final IEslProtocolListener listener;
	public InboundClientHandler(String password, IEslProtocolListener listener) {
		this.password = password;
		this.listener = listener;
	}

	@Override
	protected void handleEslEvent(ChannelHandlerContext ctx, EslEvent event) {
		log.debug("Received event: [{}]", event);
		listener.eventReceived(new Context(ctx.channel(), this), event);
	}

	@Override
	protected void handleAuthRequest(ChannelHandlerContext ctx) {
		log.debug("Auth requested, sending [auth {}]", "*****");

		sendApiSingleLineCommand(ctx.channel(), "auth " + password)
				.thenAccept(response -> {
					log.debug("Auth response [{}]", response);
					if (response.getContentType().equals(EslHeaders.Value.COMMAND_REPLY)) {
						final CommandResponse commandResponse = new CommandResponse("auth " + password, response);
						listener.authResponseReceived(commandResponse);
					} else {
						log.error("Bad auth response message [{}]", response);
						throw new IllegalStateException("Incorrect auth response");
					}
				});
	}

	@Override
	protected void handleDisconnectionNotice() {
		log.debug("Received disconnection notice");
		listener.disconnected();
	}

	@Override
	protected void handleDisconnect(ChannelHandlerContext ctx) {
		listener.disconnected();
	}

}