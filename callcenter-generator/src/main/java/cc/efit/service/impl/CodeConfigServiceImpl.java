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
package cc.efit.service.impl;

import cc.efit.db.utils.QueryHelp;
import cc.efit.repository.ColumnInfoRepository;
import cc.efit.service.dto.CodeConfigQueryCriteria;
import cc.efit.json.utils.JsonUtils;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.domain.CodeConfig;
import cc.efit.repository.CodeConfigRepository;
import cc.efit.service.CodeConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings({"unchecked","all"})
public class CodeConfigServiceImpl implements CodeConfigService {

    private final CodeConfigRepository codeConfigRepository;
    private final ColumnInfoRepository columnInfoRepository;

    @Override
    public CodeConfig find(String tableName) {
        CodeConfig genConfig = codeConfigRepository.findByTableName(tableName);
        if(genConfig == null){
            return new CodeConfig();
        }
        return genConfig;
    }

    @Override
    public CodeConfig update(CodeConfig genConfig) {
        Map<String,Object>  params = new HashMap<>();
        params.put("treeCode",genConfig.getTreeCode());
        params.put("treeName",genConfig.getTreeName());
        params.put("treeParentCode",genConfig.getTreeParentCode());
        params.put("parentMenuId",genConfig.getParentMenuId());
        genConfig.setOptions(JsonUtils.toJsonString(params));
        columnInfoRepository.saveAll(genConfig.getColumns());
        return codeConfigRepository.save(genConfig);
    }

    @Override
    public PageResult<CodeConfig> queryAll(CodeConfigQueryCriteria criteria, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<CodeConfig> page = codeConfigRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public void deleteById(Integer id) {
        codeConfigRepository.deleteById(id);
        columnInfoRepository.deleteByTableId(id);
    }
}
