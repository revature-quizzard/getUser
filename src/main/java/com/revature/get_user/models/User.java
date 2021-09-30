package com.revature.get_user.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@DynamoDbBean
public class User {

    private String id;
    private String username;
    private List<SetDocument> favorite_sets;
    private List<SetDocument> created_sets;
    private String profile_picture;
    private int points;
    private int wins;
    private int losses;
    private String registration_date;
    private List<String> gameRecord;

    public User() {
        super();
    }
}
