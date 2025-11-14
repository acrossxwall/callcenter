package cc.efit.dialogue.biz.service.impl;

import cc.efit.config.properties.FileProperties;
import cc.efit.dialogue.api.enums.TemplateVerbalEnum;
import cc.efit.dialogue.biz.config.TemplateConfig;
import cc.efit.dialogue.biz.domain.CallTemplate;
import cc.efit.dialogue.biz.repository.CallTemplateRepository;
import cc.efit.dialogue.biz.utils.FilePathUtils;
import cc.efit.dialogue.biz.vo.verbal.TemplateVerbalVo;
import cc.efit.domain.LocalStorage;
import cc.efit.exception.BadRequestException;
import cc.efit.repository.LocalStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cc.efit.dialogue.biz.service.mapstruct.TemplateVerbalMapper;
import cc.efit.dialogue.biz.repository.TemplateVerbalRepository;
import cc.efit.dialogue.biz.service.dto.TemplateVerbalDto;
import cc.efit.dialogue.biz.service.dto.TemplateVerbalQueryCriteria;
import cc.efit.dialogue.biz.domain.TemplateVerbal;
import cc.efit.dialogue.biz.service.TemplateVerbalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import cc.efit.utils.PageResult;
import cc.efit.utils.PageUtil;
import cc.efit.db.utils.QueryHelp;
import cc.efit.utils.FileUtil;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import jakarta.servlet.http.HttpServletResponse;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 话术Service业务层处理
 * 
 * @author across
 * @date 2025-08-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateVerbalServiceImpl implements TemplateVerbalService  {

    private final TemplateVerbalRepository templateVerbalRepository;
    private final TemplateVerbalMapper templateVerbalMapper;
    private final LocalStorageRepository localStorageRepository;
    private final CallTemplateRepository callTemplateRepository;
    private final FileProperties fileProperties;
    private final TemplateConfig templateConfig;
    @Override
    public PageResult<TemplateVerbalDto> queryAll(TemplateVerbalQueryCriteria criteria, Pageable pageable){
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        pageable =   PageRequest.of(pageable.getPageNumber(), pageable.getPageSize() , sort  );
        Page<TemplateVerbal> page = templateVerbalRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(templateVerbalMapper::toDto));
    }

    @Override
    public List<TemplateVerbalDto> queryAll(TemplateVerbalQueryCriteria criteria){
        return templateVerbalMapper.toDto(templateVerbalRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }
    /**
     * 查询话术
     * 
     * @param id 话术主键
     * @return 话术
     */
    @Override
    public TemplateVerbalDto selectTemplateVerbalById(Integer id)  {
        return templateVerbalMapper.toDto(templateVerbalRepository.findById(id).orElseGet(TemplateVerbal::new));
    }


    /**
     * 新增话术
     * 
     * @param templateVerbal 话术
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTemplateVerbal(TemplateVerbal templateVerbal) {
        templateVerbalRepository.save(templateVerbal);
    }

    /**
     * 修改话术
     * 
     * @param templateVerbal 话术
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateVerbal(TemplateVerbal templateVerbal) {
        templateVerbalRepository.save(templateVerbal);
    }

    /**
     * 批量删除话术
     * 
     * @param ids 需要删除的话术主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateVerbalByIds(Integer[] ids) {
        for (Integer id : ids) {
            deleteTemplateVerbalById(id);
        }
    }

    /**
     * 删除话术信息
     * 
     * @param id 话术主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplateVerbalById(Integer id) {
        templateVerbalRepository.logicDeleteById(id);
    }


    @Override
    public void download(List<TemplateVerbalDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateVerbalDto templateVerbal : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("话术编号",  templateVerbal.getId());
            map.put("模板id",  templateVerbal.getCallTemplateId());
            map.put("话术名称",  templateVerbal.getName());
            map.put("话术内容",  templateVerbal.getContent());
            map.put("类型", TemplateVerbalEnum.Type.getDescByCode(templateVerbal.getType()));
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handlerBatchImportTemplateVerbal(TemplateVerbalVo vo) {
        LocalStorage localStorage = localStorageRepository.findById(vo.fileId()).orElseThrow(()->new BadRequestException("文件不存在"));
        String filePath = localStorage.getPath();
        if (StringUtils.isBlank(filePath)) {
            //do nothing
            return;
        }
        CallTemplate callTemplate = callTemplateRepository.findById(vo.callTemplateId()).orElseThrow(()->new BadRequestException("模板不存在"));
        log.info("开始导入话术模板，文件路径：{}", filePath);
        File file = new File(filePath);
        if (!file.exists() ) {
            return  ;
        }
        Map<String, String> fileMap = unzipVerbalFile(file, callTemplate.getOrgId(), vo.callTemplateId());
        log.info("解压文件完成，文件路径：{}", filePath);
        for (Map.Entry<String, String> entry : fileMap.entrySet()) {
            String fileName = entry.getKey();
            String fileRecordingPath = entry.getValue();
            try {
                Integer verbalId = Integer.valueOf(fileName);
                templateVerbalRepository.updateTemplateVerbalRecordingById(verbalId, fileRecordingPath);
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private Map<String, String> unzipVerbalFile(File file, Integer orgId, Integer callTemplateId) {
        Map<String,String> result = new HashMap<>();
        String relativePath = FilePathUtils.getTemplateFileRelativePath(templateConfig.getVerbalPath(),orgId,callTemplateId);
        String filePath = fileProperties.getPath().getPath() + relativePath;
        FileUtil.mkdir(filePath);
        try (ZipInputStream in = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry ;
            while ((zipEntry = in.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()) {
                    String fileName = zipEntry.getName();
                    fileName = FileUtil.getFileNameByPath(fileName);
                    String currentRelativePath = relativePath + fileName;
                    Path path  = Paths.get(filePath, fileName);
                    File outputFile = path.toFile();
                    IOUtils.copy(in, new FileOutputStream(outputFile));
                    fileName = FileUtil.getFileNameNoEx(fileName);
                    result.put(fileName, currentRelativePath);
                }
            }
        }catch (Exception e){
            throw new BadRequestException("文件解压失败");
        }finally {
            if (file.exists()) {
                file.delete();
            }
        }
        return result;
    }

}
