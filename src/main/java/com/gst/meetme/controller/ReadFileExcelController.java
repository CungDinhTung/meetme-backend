package com.gst.meetme.controller;

import com.gst.meetme.model.User;
import lombok.extern.log4j.Log4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/file")
@Log4j
public class ReadFileExcelController {

    @GetMapping(value = {"/read-excel"}, consumes = {MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.APPLICATION_JSON})
    public List<User> uploadAvatar(@RequestParam("locationPath") String locationPath) {
        try {
            log.info("*********************readExcelFile*********************  :" + locationPath);
            // Tạo đối tượng File từ đường dẫn
            File file = new File(locationPath);
            log.info("*********************file*********************  : " + file.getTotalSpace());

            // Tạo đối tượng FileInputStream để đọc file
            FileInputStream fis = new FileInputStream(file);
            log.info("*********************FileInputStream********************* ");

            // Tạo đối tượng Workbook từ file Excel
            Workbook workbook = new XSSFWorkbook(fis);
            log.info("*********************XSSFWorkbook********************* ");

            // Lấy ra sheet đầu tiên từ Workbook
            Sheet sheet = workbook.getSheetAt(0);
            log.info("*********************sheet********************* ");

            // Lặp qua từng dòng trong sheet
            List<User> list = new ArrayList<>();
            for (Row row : sheet) {
                User user = new User();
                user.setUsername(row.getCell(0).getStringCellValue());
                user.setName(row.getCell(1).getStringCellValue());
                list.add(user);
            }
            log.info("*********************END*********************  : {}");
            fis.close();
            workbook.close();
            return list;
        } catch (IOException e) {
            log.error("*********************ERROR*********************  : {}");
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
