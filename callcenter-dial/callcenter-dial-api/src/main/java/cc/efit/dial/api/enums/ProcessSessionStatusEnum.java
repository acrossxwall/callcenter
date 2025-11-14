package cc.efit.dial.api.enums;

public enum ProcessSessionStatusEnum {
    /**
     * 开始呼叫
     */
    DIALED,
    /**
     * 创建通道
     */
    CREATED,
    /**
     * 应答
     */
    ANSWER,
    /**
     * 播音中
     */
    PLAYING,
    /**
     * 等待客户输入
     */
    WAIT_CUSTOMER_INPUT,
    /**
     *  客户输入asr 识别结果
     */
    CUSTOMER_INPUT,
    /**
     * 等待超时
     */
    WAIT_TIMEOUT,
    /**
     * 等待坐席输入
     */
    WAIT_AGENT_INPUT,
    /**
     * 转接坐席
     */
    TRANSFER_AGENT,
    /**
     * 挂断
     */
    HANGUP,
}
