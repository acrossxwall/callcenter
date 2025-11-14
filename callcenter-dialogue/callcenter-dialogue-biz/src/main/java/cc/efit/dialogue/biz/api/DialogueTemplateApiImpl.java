package cc.efit.dialogue.biz.api;

import cc.efit.dialogue.api.service.DialogueTemplateApi;
import cc.efit.dialogue.api.vo.template.TemplateInfo;
import cc.efit.dialogue.api.vo.template.TemplateInitInfo;
import cc.efit.dialogue.biz.service.CallTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DialogueTemplateApiImpl implements DialogueTemplateApi {

    private final CallTemplateService callTemplateService;
    @Override
    public TemplateInfo findTemplateInfoById(Integer callTemplateId) {
        return callTemplateService.findTemplateInfoByCallTemplateId(callTemplateId);
    }

    @Override
    public TemplateInitInfo initTemplateInfoToRedis(Integer callTemplateId) {
        return callTemplateService.initTemplateInfoToRedis(callTemplateId);
    }

    @Override
    public List<String> getCallTemplateCustomerDict(Integer callTemplateId) {
        //TODO 根据模板id获取模板中可用的客户字典
        return List.of("手机号码","姓名","性别");
    }
}
