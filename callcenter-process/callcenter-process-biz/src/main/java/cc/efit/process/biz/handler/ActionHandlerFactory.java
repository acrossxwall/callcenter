package cc.efit.process.biz.handler;

public interface ActionHandlerFactory {

    BaseActionHandler getHandler(Integer action);
}
