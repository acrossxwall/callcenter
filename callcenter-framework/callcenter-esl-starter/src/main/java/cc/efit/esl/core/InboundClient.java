package cc.efit.esl.core;

import cc.efit.esl.core.message.EslMessage;
import cc.efit.esl.core.exception.InboundTimeoutException ;
import java.util.function.Consumer;

public interface InboundClient {


    EslMessage sendSyncApiCommand(String address, String command, String arg);

    EslMessage sendSyncApiCommand(String address, String command, String arg, long timeoutSeconds) throws InboundTimeoutException;

    void sendSyncApiCommand(String address, String command, String arg, Consumer<EslMessage> consumer);

    String sendAsyncApiCommand(String address, String command, String arg);

    void sendAsyncApiCommand(String address, String command, String arg, Consumer<String> consumer);
}
