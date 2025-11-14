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
package cc.efit.modules.system.service.impl;

import cc.efit.db.utils.QueryHelp;
import cc.efit.redis.utils.RedisUtils;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import cc.efit.utils.PageResult;
import cc.efit.modules.system.domain.DictDetail;
import cc.efit.modules.system.repository.DictRepository;
import cc.efit.modules.system.service.dto.DictDetailQueryCriteria;
import cc.efit.utils.*;
import cc.efit.modules.system.repository.DictDetailRepository;
import cc.efit.modules.system.service.DictDetailService;
import cc.efit.modules.system.service.dto.DictDetailDto;
import cc.efit.modules.system.service.mapstruct.DictDetailMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
*
* @date 2019-04-10
*/
@Service
@RequiredArgsConstructor
public class DictDetailServiceImpl implements DictDetailService {

    private final DictRepository dictRepository;
    private final DictDetailRepository dictDetailRepository;
    private final DictDetailMapper dictDetailMapper;
    private final RedisUtils redisUtils;

    @Override
    public PageResult<DictDetailDto> queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        Page<DictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetail resources) {
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = dictDetailRepository.findById(resources.getId()).orElseGet(DictDetail::new);
        ValidationUtil.isNull( dictDetail.getId(),"DictDetail","id",resources.getId());
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    public List<DictDetailDto> getDictByName(String name) {
        String key = CacheKey.DICT_NAME + name;
        List<DictDetail> dictDetails = redisUtils.getList(key, DictDetail.class);
        if(CollUtil.isEmpty(dictDetails)){
            dictDetails = dictDetailRepository.findByDictType(name);
            redisUtils.set(key, dictDetails, 1 , TimeUnit.DAYS);
        }
        return dictDetailMapper.toDto(dictDetails);
    }

    @Override
    public DictDetailDto findById(Integer id) {
        return dictDetailMapper.toDto(dictDetailRepository.findById(id).orElseGet(DictDetail::new));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        DictDetail dictDetail = dictDetailRepository.findById(id).orElseGet(DictDetail::new);
        // 清理缓存
        delCaches(dictDetail);
        dictDetailRepository.logicDeleteById(id);
    }

    public void delCaches(DictDetail dictDetail){
        redisUtils.del(CacheKey.DICT_NAME + dictDetail.getDictType());
    }
}
