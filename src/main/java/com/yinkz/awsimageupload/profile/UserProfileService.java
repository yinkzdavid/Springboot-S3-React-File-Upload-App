package com.yinkz.awsimageupload.profile;

import com.yinkz.awsimageupload.bucket.BucketName;
import com.yinkz.awsimageupload.filestore.FileStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
//import java.util.logging.Logger;

@Service
public class UserProfileService {

    private final UserProfileDataAcessService userProfileDataAcessService;

    Logger logger = LoggerFactory.getLogger(UserProfileService.class);

    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAcessService userProfileDataAcessService,
                              FileStore fileStore) {
        this.userProfileDataAcessService = userProfileDataAcessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAcessService.getUserProfiles();
    }


    void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
      //1. check if image is not empty
//        if (file == null){
//
//            logger.info("Image is empty");
//
//        }
        isFileEmpty(file);
        //2. if file is an image

        isImage(file);
        //3. The user exists in our database

        UserProfile user = getUserProfileOrThrow(userProfileId);
        //4. Grab some metadata if any

        Map<String, String> metadata = extractMetadata(file);

        //5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(filename);
        } catch (IOException e) {
           throw new IllegalStateException(e);
        }

    }

    private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-size", String.valueOf(file.getSize()));
        return metadata;
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return userProfileDataAcessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User profile %s not in the database", userProfileId)));
    }

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(),
                IMAGE_PNG.getMimeType(),
                IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException("file must be image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()){
            throw new IllegalStateException("cannot upload empty file [ " + file.getSize() + "]");
        }
    }
}