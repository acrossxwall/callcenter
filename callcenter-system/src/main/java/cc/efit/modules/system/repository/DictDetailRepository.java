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
package cc.efit.modules.system.repository;

import cc.efit.db.base.LogicDeletedRepository;
import cc.efit.modules.system.domain.DictDetail;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
*
* @date 2019-04-10
*/
public interface DictDetailRepository extends LogicDeletedRepository<DictDetail, Integer>, JpaSpecificationExecutor<DictDetail> {

    /**
     * 根据字典名称查询
     * @param dictType /
     * @return /
     */
    List<DictDetail> findByDictType(String dictType);
}