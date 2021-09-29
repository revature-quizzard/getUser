package com.revature.get_user;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.SneakyThrows;

public class UserRepository {

    private final DynamoDBMapper dbReader;

    public UserRepository() {
        dbReader = new DynamoDBMapper(AmazonDynamoDBClientBuilder.defaultClient());
    }

    @SneakyThrows
    public User findUserById(String id) {
        User user = dbReader.load(User.class, id);
        return user;
    }
}
