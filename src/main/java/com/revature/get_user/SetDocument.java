package com.revature.get_user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBDocument
public class SetDocument {
    private String id;
    private String name;
    private List<Tag> tags;
    private boolean isPublic;
    private int views;
    private int plays;
    private int studies;
    private int favorites;
}
