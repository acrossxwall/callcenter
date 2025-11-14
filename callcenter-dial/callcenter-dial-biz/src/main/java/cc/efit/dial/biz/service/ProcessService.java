package cc.efit.dial.biz.service;

import cc.efit.process.api.req.ChatProcessReq;
import cc.efit.process.api.res.ChatProcessRes;

public interface ProcessService {

    ChatProcessRes requestProcessChat( ChatProcessReq req);
}
