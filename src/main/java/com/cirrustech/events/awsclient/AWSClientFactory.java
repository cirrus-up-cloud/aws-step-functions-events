package com.cirrustech.events.awsclient;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsAsyncClientBuilder;
import com.cirrustech.events.utils.SystemPropertiesUtils;

/**
 * Factory methods for AWS Clients.
 * <p/>
 * This is useful for testing purpose.
 */
    public class AWSClientFactory {

    private static final String AWS_REGION = "awsRegion";

    /**
     * Default region - Ireland/EU-WEST-1
     */
    private static final String DEFAULT_REGION = Regions.EU_WEST_1.getName();

    /**
     * Factory method for AWS Step Functions client.
     *
     * @return the AWS Step Functions client.
     */
    public static AWSStepFunctions getClient() {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(3 * 1000);
        clientConfiguration.setSocketTimeout(3 * 1000);

        AWSStepFunctionsAsyncClientBuilder standard = AWSStepFunctionsAsyncClientBuilder.standard();
        standard.setClientConfiguration(clientConfiguration);

        if (SystemPropertiesUtils.hasPropertySet(AWS_REGION)) {

            standard.setRegion(SystemPropertiesUtils.getPropertyValue(AWS_REGION));
        } else {

            standard.setRegion(DEFAULT_REGION);
        }

        return standard.build();
    }
}
