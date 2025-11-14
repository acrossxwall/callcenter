package cc.efit.dialogue.biz.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import cc.efit.db.base.BaseCommonConstant;
import cc.efit.data.permission.DataPermissionEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
/**
 * 话术对象 efit_template_verbal
 * 
 * @author across
 * @date 2025-08-18
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name="efit_template_verbal")
@SQLRestriction(BaseCommonConstant.DEFAULT_DELETE)
public class TemplateVerbal extends DataPermissionEntity implements Serializable {

    /** 主键 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
}
