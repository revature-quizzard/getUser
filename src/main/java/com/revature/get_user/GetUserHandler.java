package com.revature.get_user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.get_user.models.User;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.Map;

public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final UserRepository userRepository;

    public GetUserHandler() {
        userRepository = new UserRepository();
    }

    public GetUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Takes a user id as a query string parameter. The id is used to query the database and
     * returns a user with the matching id. If the user isn't found, return a 400 level status code.
     *
     * @param requestEvent will contain query parameters for querying DynamoDB
     * @return
     * @author Robert Ni
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        
        // Setting Cors headers to bypass API Gateway Issue
        Map<String, String> headers = new HashMap<>();
        headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization");
        headers.put("Access-Control-Allow-Origin", "*");
        responseEvent.setHeaders(headers);

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        // get the query string params
        Map<String, String> queryStringParams = requestEvent.getQueryStringParameters();

        // checks if params are null - if null, return a 400 level status code
        if (queryStringParams == null) {
            responseEvent.setStatusCode(HttpStatusCode.BAD_REQUEST);
            responseEvent.setBody("Missing query string - (ex. /users?id=valid_user_id)");
            return responseEvent;
        }

        // attempt to find the User in the database
        User user = userRepository.findUserById(queryStringParams.get("id"));

        // map the user in response if available, else return a 400 level status code
        if (user != null) {
            responseEvent.setBody(mapper.toJson(user));
        } else {
            responseEvent.setStatusCode(HttpStatusCode.NOT_FOUND);
            responseEvent.setBody("No user with that id found");
            return responseEvent;
        }

        responseEvent.setStatusCode(HttpStatusCode.OK);
        return responseEvent;
    }
}
