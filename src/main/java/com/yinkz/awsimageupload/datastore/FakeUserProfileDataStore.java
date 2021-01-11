package com.yinkz.awsimageupload.datastore;


import com.yinkz.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static  final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.fromString("aa14c229-02d3-48fa-b7c7-b15499a6633f"), "Debby", null));
        USER_PROFILES.add(new UserProfile(UUID.fromString("bc2a20b7-73b6-41b9-9aaf-2046bd970f24"), "Gbengus", null ));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
