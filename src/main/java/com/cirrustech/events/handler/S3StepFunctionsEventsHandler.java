package com.cirrustech.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.cirrustech.events.awsclient.AWSClientFactory;
import com.cirrustech.events.utils.SystemPropertiesUtils;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_SYSTEM_PROPERTY;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_SYSTEM_PROPERTY;

/**
 * Lambda handler that triggers a state machine when an S3 event occurs.
 */
public class S3StepFunctionsEventsHandler implements RequestHandler<S3Event, Void> {

    private static final String STATE_MACHINE_ARN = "stateMachineARN";

    /**
     * {@inheritDoc}
     */
    @Override
    public Void handleRequest(S3Event s3Event, Context context) {

        context.getLogger().log("Starting Lambda functions.");
        validateInputParameters();

        String input = s3Event.toJson();
        AWSStepFunctions client = AWSClientFactory.getClient();
        String stateMachineArn = SystemPropertiesUtils.getPropertyValue(STATE_MACHINE_ARN);

        StartExecutionRequest request = new StartExecutionRequest()
                .withInput(input)
                .withStateMachineArn(stateMachineArn);

        context.getLogger().log("Running state machine arn: " + stateMachineArn + " with input: " + input);

        StartExecutionResult result = client.startExecution(request);

        context.getLogger().log("State machine started with execution ARN: " + result.getExecutionArn());
        return null;
    }

    private void validateInputParameters() {

        if (!SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)
                || !SystemPropertiesUtils.hasPropertySet(SECRET_KEY_SYSTEM_PROPERTY)) {

            throw new IllegalArgumentException("Missing AWS credentials for Step Functions.");
        }

        if (!SystemPropertiesUtils.hasPropertySet(STATE_MACHINE_ARN)) {

            throw new IllegalArgumentException("Missing state machine ARN.");
        }
    }
}
