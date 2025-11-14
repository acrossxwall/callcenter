package cc.efit.dialogue.api.constants;

public class TemplateConstants {
    public static final String DEFAULT_INTENTION_LEVEL = "T";
    public static final String DEFAULT_INTENTION_LEVEL_DESC = "拒接";
    public static final String INTENTION_BRANCH_DEFAULT = "默认";

    public static final String INTENTION_BRANCH_YES = "肯定";

    public static final String INTENTION_BRANCH_NO = "否定";
    /** 交互全局设置  start */
    public static final Integer NOT_INTERRUPT_SECONDS = 15;
    public static final Integer MAX_INTERACTIVE_COUNT = 10;

    public static final Integer MAX_DURATION = 10;

    public static final Integer INTERACTION_ACTION_HANGUP = 1;

    public static final String INTERACTION_VERBAL_CONTENT = "那我们就不打扰您了，再见";
    public static final String INTERACTION_VERBAL_RECORD_PATH = "interaction.wav";
    /** 交互全局设置  end */
    /** 无应答全局设置  start */
    public static final Integer MAX_NO_REPLY_COUNT = 3;
    public static final Integer MAX_NO_REPLY_SECONDS = 60;
    public static final Integer NO_REPLY_ACTION_HANGUP = 1;
    public static final String NO_VERBAL_CONTENT = "这边听不到您说话，那我们就不打扰您了，再见";
    /** 交互全局设置  end */
    /** 全局兜底设置 */
    public static final String DEFAULT_VERBAL_CONTENT = "您好，还在么？";
    public static final Integer DEFAULT_VERBAL_ACTION_HANGUP = 1;
    public static final String PREDICT_TYPE_KEYWORD = "keyword";

    public static final String PREDICT_TYPE_NLU = "nlu";

    public  static final String CUSTOMER_DICT_PHONE = "手机号码";

}
