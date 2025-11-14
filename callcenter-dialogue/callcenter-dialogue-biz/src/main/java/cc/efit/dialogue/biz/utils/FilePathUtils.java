package cc.efit.dialogue.biz.utils;

public class FilePathUtils {

    public static String getTemplateFileAbsolutePath(String base,String relativePath,String fileName){
        return base + relativePath + fileName ;
    }

    public static String getTemplateFileRelativePath( String templatePath,Integer orgId,Integer callTemplateId ){
        return String.format(templatePath,orgId,callTemplateId) ;
    }
}
