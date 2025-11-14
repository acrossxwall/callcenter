package cc.efit.dial.biz.rest;

import cc.efit.dial.api.req.DialPhoneReq;
import cc.efit.dial.biz.service.DialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dial")
public class DialController {
    @Autowired
    private DialService dialService;

    /**
     * 单个测试使用
     */
    @PostMapping("/phone")
    public String dialPhone(@RequestBody DialPhoneReq req) {
        dialService.dialPhone(req);
        return "success";
    }
}
