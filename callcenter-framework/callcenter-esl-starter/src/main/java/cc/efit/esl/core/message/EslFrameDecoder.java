package cc.efit.esl.core.message;

import cc.efit.esl.core.exception.EslDecoderException;
import cc.efit.esl.core.utils.ByteBuilder;
import cc.efit.esl.core.utils.HeaderParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

@Slf4j
public class EslFrameDecoder extends ReplayingDecoder<EslFrameDecoder.State> {
    private static final byte LF = 10;
    private final int maxHeaderSize;
    private EslMessage currentMessage;
    private boolean treatUnknownHeadersAsBody = false;

    public EslFrameDecoder(int maxHeaderSize) {
        super(State.READ_HEADER);
        if (maxHeaderSize <= 0) {
            throw new IllegalArgumentException(
                    "maxHeaderSize must be a positive integer: " +
                            maxHeaderSize);
        }
        this.maxHeaderSize = maxHeaderSize;
    }

    /**
     * <p>Constructor for EslFrameDecoder.</p>
     *
     * @param maxHeaderSize             a int.
     * @param treatUnknownHeadersAsBody a boolean.
     */
    public EslFrameDecoder(int maxHeaderSize, boolean treatUnknownHeadersAsBody) {
        this(maxHeaderSize);
        this.treatUnknownHeadersAsBody = treatUnknownHeadersAsBody;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        if (log.isDebugEnabled()) {
            log.debug("decode() : state [{}]", state());
        }
        switch (state()) {
            case READ_HEADER:
                if (currentMessage == null) {
                    currentMessage = new EslMessage();
                }
                /*
                 *  read '\n' terminated lines until reach a single '\n'
                 */
                boolean reachedDoubleLF = false;
                while (!reachedDoubleLF) {
                    // this will read or fail
                    String headerLine = readToLineFeedOrFail(buffer, maxHeaderSize);
                    if (log.isDebugEnabled()) {
                        log.debug("read header line [{}]", headerLine);
                    }

                    if (!headerLine.isEmpty()) {
                        // split the header line
                        String[] headerParts = HeaderParser.splitHeader(headerLine);
                        EslHeaders.Name headerName = EslHeaders.Name.fromLiteral(headerParts[0]);
                        if (headerName == null) {
                            if (treatUnknownHeadersAsBody) {
                                // cache this 'header' as a body line <-- useful for Outbound client mode
                                currentMessage.addBodyLine(headerLine);
                            } else {
                                throw new IllegalStateException("Unhandled ESL header [" + headerParts[0] + ']');
                            }
                        }
                        currentMessage.addHeader(headerName, headerParts[1]);
                    } else {
                        reachedDoubleLF = true;
                    }
                    // do not read in this line again
                    checkpoint();
                }
                // have read all headers - check for content-length
                if (currentMessage.hasContentLength()) {
                    checkpoint(State.READ_BODY);
                    if (log.isDebugEnabled()){
                        log.debug("have content-length, decoding body ..");
                    }

                    //  force the next section
                } else {
                    // end of message
                    checkpoint(State.READ_HEADER);
                    // send message upstream
                    EslMessage decodedMessage = currentMessage;
                    currentMessage = null;

                    out.add(decodedMessage);
                }
                return;
            case READ_BODY:
                /*
                 *   read the content-length specified
                 */
                int contentLength = currentMessage.getContentLength();
                ByteBuf bodyBytes = buffer.readBytes(contentLength);
                if (log.isDebugEnabled()) {
                    log.debug("read [{}] body bytes", bodyBytes.writerIndex());
                }

                // most bodies are line based, so split on LF
                while (bodyBytes.isReadable()) {
                    String bodyLine = readLine(bodyBytes, contentLength);
                    if (log.isDebugEnabled()) {
                        log.debug("read body line [{}]", bodyLine);
                    }

                    currentMessage.addBodyLine(bodyLine);
                }
                // release bodyBytes
                if (bodyBytes.refCnt() > 0) {
                    ReferenceCountUtil.safeRelease(bodyBytes);
                }

                // end of message
                checkpoint(State.READ_HEADER);
                // send message upstream
                EslMessage decodedMessage = currentMessage;
                currentMessage = null;

                out.add(decodedMessage);
                return;
            default:
                throw new EslDecoderException("Illegal state: [" + state() + ']');
        }
    }

    private String readToLineFeedOrFail(ByteBuf buffer, int maxLineLegth) {
        ByteBuilder sb =  ByteBuilder.newBuilder();
        while (true) {
            // this read might fail
            byte nextByte = buffer.readByte();
            if (nextByte == LF) {
                return sb.string();
            } else {
                // Abort decoding if the decoded line is too large.
                if (sb.length() >= maxLineLegth) {
                    throw new TooLongFrameException(
                            "ESL header line is longer than " + maxLineLegth + " bytes.");
                }
                sb.append( nextByte);
            }
        }
    }

    private String readLine(ByteBuf buffer, int maxLineLength) {
        ByteBuilder sb =  ByteBuilder.newBuilder();
        while (buffer.isReadable()) {
            // this read should always succeed
            byte nextByte = buffer.readByte();
            if (nextByte == LF) {
                return sb.toString();
            } else {
                // Abort decoding if the decoded line is too large.
                if (sb.length() >= maxLineLength) {
                    throw new TooLongFrameException(
                            "ESL message line is longer than " + maxLineLength + " bytes.");
                }
                sb.append( nextByte);
            }
        }

        return sb.string();
    }

    protected enum State {
        /**
         * 包头
         */
        READ_HEADER,
        /**
         * 包体
         */
        READ_BODY,
    }
}
