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
package cc.efit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 */
public class ColUtil {
    private static final Map<String,String> config = new HashMap<>();
    static {
        config.put("tinyint","Integer");
        config.put("smallint","Integer");
        config.put("mediumint","Integer");
        config.put("int","Integer");
        config.put("integer","Integer");
        config.put("bigint","Long");
        config.put("float","Float");
        config.put("double","Double");
        config.put("decimal","BigDecimal");
        config.put("bit","Boolean");
        config.put("char","String");
        config.put("varchar","String");
        config.put("tinytext","String");
        config.put("text","String");
        config.put("mediumtext","String");
        config.put("longtext","String");
        config.put("date","LocalDateTime");
        config.put("datetime","LocalDateTime");
        config.put("timestamp","LocalDateTime");
    }
    /**
     * 转换mysql数据类型为java数据类型
     *
     * @param type 数据库字段类型
     * @return String
     */
    static String cloToJava(String type) {
        assert type != null;
        type = type.toLowerCase().replaceAll("\\d+","");
        return config.getOrDefault(type, "");
    }

}
