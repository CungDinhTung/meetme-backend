package com.gst.meetme.controller;

import com.gst.util.javase.io.FileUtil;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
@Log4j
public class UserController {

    //    public static final String IMAGE_STORAGE_PATH = SystemPropertiesProvider.getInstance().getBaseHost()
//            + SystemPropertiesProvider.getInstance().getReplacementFolderName();
    public static final String IMAGE_STORAGE_PATH = "/u01/meetme/";

    @PostMapping(value = {"/upload-avatar"}, consumes = {MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.APPLICATION_JSON})
    public List<String> uploadAvatar(@RequestParam("files") MultipartFile[] multipartFiles,
                                     @RequestParam("username") String username,
                                     @RequestParam("imagesOld") List<String> imagesOld) {
        List<String> listUrlImage = new ArrayList<>();
        try {
            removeImageOld(imagesOld);
            for (int i = 0; i < multipartFiles.length; i++) {
                log.info("\n --------filename : " + multipartFiles[i].getOriginalFilename() + "size : " + multipartFiles[i].getSize());
                listUrlImage.add(
                        FileUtil.getInstance().saveFile(IMAGE_STORAGE_PATH, username + "_images_" + i,
                                FilenameUtils.getExtension(multipartFiles[i].getOriginalFilename()), multipartFiles[i].getInputStream())
                );
            }
        } catch (Exception e) {
            log.debug("Exception {}", e);
        }
        return listUrlImage;
    }

    private void removeImageOld(List<String> listImageOld) {
        for (String imageOld : listImageOld) {
            if (StringUtils.isNotEmpty(imageOld)) {
                File f = new File(imageOld);
                f.delete();
            }
        }
    }

}
