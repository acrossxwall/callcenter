package cc.efit.job.core;

public record XxlJobResponse (
      int code,

      String msg,

      String content
){
    public boolean success(){
        return code == 200;
    }
}
