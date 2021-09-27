package com.revature.get_user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public class GetUserHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Gson mapper = new GsonBuilder().setPrettyPrinting().create();
    private final UserRepository userRepository = UserRepository.getInstance();

    /**
     * This function takes in a user id as a query parameter. The id is used to query
     * DynamoDB and return a user with the matching id.
     *
     * @param requestEvent will contain query parameters for querying DynamoDB
     * @return
     * @author Robert Ni
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent requestEvent, Context context) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();

        LambdaLogger logger = context.getLogger();
        logger.log("RECEIVED EVENT: " + requestEvent);

        Map<String, String> pathParams = requestEvent.getQueryStringParameters();

        if (pathParams == null) {
            responseEvent.setStatusCode(400);
            return responseEvent;
        }

        User user = userRepository.findUserById(pathParams.get("id"));

        if (user != null) {
            responseEvent.setBody(mapper.toJson(user));
        } else {
            responseEvent.setStatusCode(400);
        }

        return responseEvent;
    }
}
