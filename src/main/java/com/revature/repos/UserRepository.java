package com.revature.repos;

public class UserRepository {

    private static final UserRepository userRepo = new UserRepository();

    public static UserRepository getInstance() {
        return userRepo;
    }
}
