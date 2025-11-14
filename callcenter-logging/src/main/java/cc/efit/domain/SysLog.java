/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cc.efit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * @date 2018-11-24
 */
@Entity
@Getter
@Setter
@Table(name = "sys_log")
@NoArgsConstructor
public class SysLog implements Serializable {

    @Id
    @Column(name = "log_id")
    @Schema(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(name = "操作用户")
    private String username;

    @Schema(name = "描述")
    private String description;

    @Schema(name = "方法名")
    private String method;

    @Schema(name = "参数")
    private String params;

    @Schema(name = "日志类型")
    private String logType;

    @Schema(name = "请求ip")
    private String requestIp;

    @Schema(name = "地址")
    private String address;

    @Schema(name = "浏览器")
    private String browser;

    @Schema(name = "请求耗时")
    private Long time;

    @Schema(name = "异常详细")
    private byte[] exceptionDetail;

    /** 创建日期 */
    @CreationTimestamp
    @Schema(name = "创建日期：yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;

    public SysLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
