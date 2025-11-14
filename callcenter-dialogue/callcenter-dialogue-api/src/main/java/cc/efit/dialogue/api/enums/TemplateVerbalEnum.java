package cc.efit.dialogue.api.enums;

public class TemplateVerbalEnum {

    public enum Source{
        NODE(1,"节点"),
        KNOWLEDGE(2,"知识库"),
        GLOBAL(3,"全局")
        ;
        private Integer code;
        private String desc;
        Source(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode(){
            return code;
        }
    }

    public enum Type{
        TTS(1,"TTS"),
        FILE(2,"录音文件"),
        ;
        private Integer code;
        private String desc;
        Type(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode(){
            return code;
        }

        public static String getDescByCode(Integer code){
            for(Type type:Type.values()){
                if(type.getCode().equals(code)){
                    return type.desc;
                }
            }
            return FILE.desc;
        }
    }

    public enum Status{
        WAIT_UPLOAD(0,"待上传"),
        UPLOAD_SUCCESS(1,"已上传"),
        ;
        private Integer code;
        private String desc;
        Status(Integer code, String desc){
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode(){
            return code;
        }
    }
}
