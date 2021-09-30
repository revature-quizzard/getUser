package com.revature.get_user;

import com.revature.get_user.models.User;
import lombok.SneakyThrows;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class UserRepository {

    public static final UserRepository userRepository = new UserRepository();
    public final DynamoDbTable<User> userTable;

    private UserRepository() {
        DynamoDbClient db = DynamoDbClient.builder().httpClient(ApacheHttpClient.create()).build();
        DynamoDbEnhancedClient dbClient = DynamoDbEnhancedClient.builder().dynamoDbClient(db).build();
        userTable = dbClient.table("Users", TableSchema.fromBean(User.class));
    }

    public static UserRepository getInstance() {
        return userRepository;
    }

    @SneakyThrows
    public User findUserById(String id) {
        AttributeValue value = AttributeValue.builder().s(id).build();
        Expression filter = Expression.builder().expression("#a = :b").putExpressionName("#a", "id").putExpressionValue(":b", value).build();
        ScanEnhancedRequest request = ScanEnhancedRequest.builder().filterExpression(filter).build();
        User user = userTable.scan(request).stream().findFirst().orElseThrow(RuntimeException::new).items().get(0);
        return user;
    }
}
