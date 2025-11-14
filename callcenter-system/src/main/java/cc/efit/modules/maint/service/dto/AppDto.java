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
package cc.efit.modules.maint.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import cc.efit.db.base.BaseDTO;
import java.io.Serializable;

/**
* @author zhanghouying
* @date 2019-08-24
*/
@Getter
@Setter
public class AppDto extends BaseDTO implements Serializable {

	@Schema(name = "ID")
    private Integer id;

	@Schema(name = "应用名称")
	private String name;

	@Schema(name = "端口")
	private Integer port;

	@Schema(name = "上传目录")
	private String uploadPath;

	@Schema(name = "部署目录")
	private String deployPath;

	@Schema(name = "备份目录")
	private String backupPath;

	@Schema(name = "启动脚本")
	private String startScript;

	@Schema(name = "部署脚本")
	private String deployScript;
}
