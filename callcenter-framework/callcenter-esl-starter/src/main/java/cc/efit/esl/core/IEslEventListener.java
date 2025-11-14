package cc.efit.esl.core;


import cc.efit.esl.core.event.EslEvent;

public interface IEslEventListener {

    void onEslEvent(Context ctx, EslEvent event);

}