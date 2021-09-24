package com.revature.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@DynamoDBTable(tableName = "Users")
public class User {

    @DynamoDBAttribute
    private String id;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private List<String> favoriteSets;

    @DynamoDBAttribute
    private List<String> createdSets;

    @DynamoDBAttribute
    private int points;

    @DynamoDBAttribute
    private int wins;

    @DynamoDBAttribute
    private int losses;

    @DynamoDBAttribute
    private LocalDateTime registrationDate;

    @DynamoDBAttribute
    private List<String> gameRecord;
}
