package com.yinkz.awsimageupload.bucket;

public enum BucketName {

    PROFILE_IMAGE("yinkz-image-upload-123");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
