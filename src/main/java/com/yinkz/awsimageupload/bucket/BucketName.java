package com.yinkz.awsimageupload.bucket;

public enum BucketName {

    PROFILE_IMAGE("yikus-image-upload-bucket");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
