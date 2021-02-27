package com.phungdung.gnoc2.common.untils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class SQLBuilder {
    public static String getSqlQueryById(String module, String queryId) {
        InputStream inputStream = null;
        try {
            String filePath = "sql" + File.separator + module + File.separator + queryId + ".sql";
            log.info("SQL file path :" + filePath);
            Resource resource = new ClassPathResource(filePath);
            inputStream = resource.getInputStream();
            if (inputStream != null) {
//                return new String(inputStream.readAllBytes());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        }
        return null;
    }
}
