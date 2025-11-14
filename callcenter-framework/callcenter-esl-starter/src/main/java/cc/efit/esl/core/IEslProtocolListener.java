package cc.efit.esl.core;

import cc.efit.esl.core.event.EslEvent;
import cc.efit.esl.core.res.CommandResponse;

public interface IEslProtocolListener {
	void authResponseReceived(CommandResponse response);

	void eventReceived(Context ctx, EslEvent event);

	void disconnected();
}