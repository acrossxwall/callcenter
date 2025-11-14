package cc.efit.esl.core;

import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.event.EslEventHeaderNames;
import cc.efit.esl.core.message.EslHeaders;
import cc.efit.esl.core.message.EslMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractEslClientHandler extends SimpleChannelInboundHandler<EslMessage> {

	public static final String MESSAGE_TERMINATOR = "\n\n";
	public static final String LINE_TERMINATOR = "\n";

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	// used to preserve association between adding future to queue and sending message on channel
	private final ReentrantLock syncLock = new ReentrantLock();
	private final ConcurrentLinkedQueue<CompletableFuture<EslMessage>> apiCalls =
			new ConcurrentLinkedQueue<>();

	private final ConcurrentHashMap<String, CompletableFuture<EslEvent>> backgroundJobs =
			new ConcurrentHashMap<>();
	private final ExecutorService backgroundJobExecutor = Executors.newCachedThreadPool();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) throws Exception {

		for (final CompletableFuture<EslMessage> apiCall : apiCalls) {
			apiCall.completeExceptionally(e.getCause());
		}

		for (final CompletableFuture<EslEvent> backgroundJob : backgroundJobs.values()) {
			backgroundJob.completeExceptionally(e.getCause());
		}

		ctx.close();

		ctx.fireExceptionCaught(e);

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, EslMessage message) throws Exception {
		final String contentType = message.getContentType();
		if (contentType.equals(EslHeaders.Value.TEXT_EVENT_PLAIN) ||
				contentType.equals(EslHeaders.Value.TEXT_EVENT_XML)) {
			//  transform into an event
			final EslEvent eslEvent = new EslEvent(message);
			if (eslEvent.getEventName().equals("BACKGROUND_JOB")) {
				final String backgroundUuid = eslEvent.getEventHeaders().get(EslEventHeaderNames.JOB_UUID);
				final CompletableFuture<EslEvent> future = backgroundJobs.remove(backgroundUuid);
				if (null != future) {
					future.complete(eslEvent);
				}
			} else {
				handleEslEvent(ctx, eslEvent);
			}
		} else {
			handleEslMessage(ctx, message);
		}
	}

	protected void handleEslMessage(ChannelHandlerContext ctx, EslMessage message) {
		log.info("Received message: [{}]", message);
		final String contentType = message.getContentType();

		switch (contentType) {
			case EslHeaders.Value.API_RESPONSE:
				log.debug("Api response received [{}]", message);
				apiCalls.poll().complete(message);
				break;

			case EslHeaders.Value.COMMAND_REPLY:
				log.debug("Command reply received [{}]", message);
				apiCalls.poll().complete(message);
				break;

			case EslHeaders.Value.AUTH_REQUEST:
				log.debug("Auth request received [{}]", message);
				handleAuthRequest(ctx);
				break;

			case EslHeaders.Value.TEXT_DISCONNECT_NOTICE:
				log.debug("Disconnect notice received [{}]", message);
				handleDisconnectionNotice();
				break;

			default:
				log.warn("Unexpected message content type [{}]", contentType);
				break;
		}
	}

	/**
	 * Synthesise a synchronous command/response by creating a callback object which is placed in
	 * queue and blocks waiting for another IO thread to process an incoming {@link EslMessage} and
	 * attach it to the callback.
	 *
	 * @param channel
	 * @param command single string to send
	 * @return the {@link EslMessage} attached to this command's callback
	 */
	public CompletableFuture<EslMessage> sendApiSingleLineCommand(Channel channel, final String command) {
		final CompletableFuture<EslMessage> future = new CompletableFuture<>();
		try {
			syncLock.lock();
			apiCalls.add(future);
			channel.writeAndFlush(command + MESSAGE_TERMINATOR);
		} finally {
			syncLock.unlock();
		}

		return future;

	}

	/**
	 * Sends a FreeSWITCH API command to the channel and blocks, waiting for an immediate response from the
	 * server.
	 * <p/>
	 * The outcome of the command from the server is returned in an {@link EslMessage} object.
	 *
	 * @param channel
	 * @param command API command to send
	 * @param arg     command arguments
	 * @return an {@link EslMessage} containing command results
	 */
	public CompletableFuture<EslMessage> sendSyncApiCommand(Channel channel, String command, String arg) {
		return sendApiSingleLineCommand(channel, "api " + command + ' ' + arg);
	}

	/**
	 * Synthesise a synchronous command/response by creating a callback object which is placed in
	 * queue and blocks waiting for another IO thread to process an incoming {@link EslMessage} and
	 * attach it to the callback.
	 *
	 * @param channel
	 * @return the {@link EslMessage} attached to this command's callback
	 */
	public CompletableFuture<EslMessage> sendApiMultiLineCommand(Channel channel, final List<String> commandLines) {
		//  Build command with double line terminator at the end
		final StringBuilder sb = new StringBuilder();
		for (final String line : commandLines) {
			sb.append(line);
			sb.append(LINE_TERMINATOR);
		}
		sb.append(LINE_TERMINATOR);

		final CompletableFuture<EslMessage> future = new CompletableFuture<>();
		try {
			syncLock.lock();
			apiCalls.add(future);
			channel.write(sb.toString());
            channel.flush();
		} finally {
			syncLock.unlock();
		}

		return future;

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		handleDisconnect(ctx);
	}

	/**
	 * Returns the Job UUID of that the response event will have.
	 *
	 * @param channel
	 * @param command
	 * @return Job-UUID as a string
	 */
	public CompletableFuture<EslEvent> sendBackgroundApiCommand(Channel channel, final String command) {

		return sendApiSingleLineCommand(channel, command)
				.thenComposeAsync(result -> {
					if (result.hasHeader(EslHeaders.Name.JOB_UUID)) {
						final String jobId = result.getHeaderValue(EslHeaders.Name.JOB_UUID);
						final CompletableFuture<EslEvent> resultFuture = new CompletableFuture<>();
						backgroundJobs.put(jobId, resultFuture);
						return resultFuture;
					} else {
						final CompletableFuture<EslEvent> resultFuture = new CompletableFuture<>();
						resultFuture.completeExceptionally(new IllegalStateException("Missing Job-UUID header in bgapi response"));
						return resultFuture;
					}
				}, backgroundJobExecutor);
	}

	protected abstract void handleEslEvent(ChannelHandlerContext ctx, EslEvent event);

	protected abstract void handleAuthRequest(ChannelHandlerContext ctx);

	protected abstract void handleDisconnectionNotice();

	protected abstract void handleDisconnect(ChannelHandlerContext ctx);
}