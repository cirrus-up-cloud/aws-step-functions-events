package com.cirrustech.events.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.model.AWSStepFunctionsException;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;
import com.amazonaws.services.stepfunctions.model.StartExecutionResult;
import com.cirrustech.events.awsclient.AWSClientFactory;
import com.cirrustech.events.utils.SystemPropertiesUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.amazonaws.SDKGlobalConfiguration.ACCESS_KEY_SYSTEM_PROPERTY;
import static com.amazonaws.SDKGlobalConfiguration.SECRET_KEY_SYSTEM_PROPERTY;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Tests for {@link S3StepFunctionsEventsHandler} class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemPropertiesUtils.class, System.class, AWSClientFactory.class})
public class S3StepFunctionsEventsHandlerTest {

    private final S3StepFunctionsEventsHandler handler = new S3StepFunctionsEventsHandler();

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAccessKeyProvided() {

        //setup
        Context context = Mockito.mock(Context.class);
        S3Event s3Event = Mockito.mock(S3Event.class);
        LambdaLogger logger = Mockito.mock(LambdaLogger.class);

        mockStatic(SystemPropertiesUtils.class);
        when(SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)).thenReturn(false);

        when(context.getLogger()).thenReturn(logger);

        //call
        handler.handleRequest(s3Event, context);


    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSecretKeyProvided() {

        //setup
        Context context = Mockito.mock(Context.class);
        S3Event s3Event = Mockito.mock(S3Event.class);
        LambdaLogger logger = Mockito.mock(LambdaLogger.class);

        mockStatic(SystemPropertiesUtils.class);
        when(SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet(SECRET_KEY_SYSTEM_PROPERTY)).thenReturn(false);

        when(context.getLogger()).thenReturn(logger);

        //call
        handler.handleRequest(s3Event, context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStateArnProvided() {

        //setup
        Context context = Mockito.mock(Context.class);
        S3Event s3Event = Mockito.mock(S3Event.class);
        LambdaLogger logger = Mockito.mock(LambdaLogger.class);

        mockStatic(SystemPropertiesUtils.class);
        when(SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet(SECRET_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet("stateMachineARN")).thenReturn(false);

        when(context.getLogger()).thenReturn(logger);

        //call
        handler.handleRequest(s3Event, context);
    }


    @Test(expected = AWSStepFunctionsException.class)
    public void testThrowAWSStepFunctionsException() {

        //setup
        Context context = Mockito.mock(Context.class);
        S3Event s3Event = Mockito.mock(S3Event.class);
        LambdaLogger logger = Mockito.mock(LambdaLogger.class);
        AWSStepFunctions stepFunctions = Mockito.mock(AWSStepFunctions.class);

        mockStatic(AWSClientFactory.class);
        mockStatic(SystemPropertiesUtils.class);
        when(SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet(SECRET_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet("stateMachineARN")).thenReturn(true);

        when(context.getLogger()).thenReturn(logger);
        when(s3Event.toJson()).thenReturn("{}");
        when(AWSClientFactory.getClient()).thenReturn(stepFunctions);
        when(stepFunctions.startExecution((StartExecutionRequest)anyObject())).thenThrow(new AWSStepFunctionsException("AWSStepFunctionsException"));

        //call
        handler.handleRequest(s3Event, context);

    }

    @Test
    public void testComplete() {

        //setup
        Context context = Mockito.mock(Context.class);
        S3Event s3Event = Mockito.mock(S3Event.class);
        LambdaLogger logger = Mockito.mock(LambdaLogger.class);
        AWSStepFunctions stepFunctions = Mockito.mock(AWSStepFunctions.class);
        StartExecutionResult result = new StartExecutionResult().withExecutionArn("value");

        mockStatic(AWSClientFactory.class);
        mockStatic(SystemPropertiesUtils.class);
        when(SystemPropertiesUtils.hasPropertySet(ACCESS_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet(SECRET_KEY_SYSTEM_PROPERTY)).thenReturn(true);
        when(SystemPropertiesUtils.hasPropertySet("stateMachineARN")).thenReturn(true);

        when(context.getLogger()).thenReturn(logger);
        when(s3Event.toJson()).thenReturn("{}");
        when(AWSClientFactory.getClient()).thenReturn(stepFunctions);
        when(stepFunctions.startExecution((StartExecutionRequest)anyObject())).thenReturn(result);

        //call
        handler.handleRequest(s3Event, context);

        //verify
        Mockito.verify(context, times(3)).getLogger();
        Mockito.verify(s3Event, times(1)).toJson();
        Mockito.verify(stepFunctions, times(1)).startExecution((StartExecutionRequest) anyObject());
        PowerMockito.verifyStatic();
    }


}
