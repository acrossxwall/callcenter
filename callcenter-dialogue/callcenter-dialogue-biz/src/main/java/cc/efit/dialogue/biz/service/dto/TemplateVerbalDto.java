package cc.efit.dialogue.biz.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
 * 话术对象 efit_template_verbal
 * 
 * @author across
 * @date 2025-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateVerbalDto extends BaseDTO implements Serializable {

    /** 主键 */
    private Integer id;

    /** 部门id */
    private Integer deptId;

    /** 用户id */
    private Integer userId;

    /** 话术模板id */
    private Integer callTemplateId;

    /** 来源名称知识库节点名字全局等 */
    private String name;

    /** 话术内容 */
    private String content;

    /** 类型1-tts 2-录音 */
    private Integer type;

    /** 1-节点 2-知识库 3-全局 */
    private Integer source;

    /** 1-已上传 0-未上传 */
    private Integer status;

    /** 文件路径 */
    private String recording;

    private Integer orgId;

}
