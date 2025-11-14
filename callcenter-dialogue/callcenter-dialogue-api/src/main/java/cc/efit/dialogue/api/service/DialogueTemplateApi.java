package cc.efit.dialogue.api.service;

import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;

import java.util.List;

public interface DialogueTemplateApi {

    TemplateInfo findTemplateInfoById(Integer callTemplateId);

    /**
     * 将模板信息初始化到缓存当中
     * @param callTemplateId
     */
    TemplateInitInfo initTemplateInfoToRedis(Integer callTemplateId);

    List<String> getCallTemplateCustomerDict(Integer callTemplateId);

}
