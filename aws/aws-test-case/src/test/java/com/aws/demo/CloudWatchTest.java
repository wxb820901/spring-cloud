package com.aws.demo;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import org.junit.Test;

public class CloudWatchTest {
    @Test
    public void testQueryMetrics(){
        final AmazonCloudWatch cw =
                AmazonCloudWatchClientBuilder.defaultClient();

        ListMetricsRequest request = new ListMetricsRequest()
                .withMetricName("")
                .withNamespace("");

        boolean done = false;

        while(!done) {
            ListMetricsResult response = cw.listMetrics(request);

            for(Metric metric : response.getMetrics()) {
                System.out.printf(
                        "Retrieved metric %s", metric.getMetricName());
            }

            request.setNextToken(response.getNextToken());

            if(response.getNextToken() == null) {
                done = true;
            }
        }
    }

    @Test
    public void testPublishMetrics(){
        final AmazonCloudWatch cw =
                AmazonCloudWatchClientBuilder.defaultClient();

        Dimension dimension = new Dimension()
                .withName("UNIQUE_PAGES")
                .withValue("URLS");

        MetricDatum datum = new MetricDatum()
                .withMetricName("PAGES_VISITED")
                .withUnit(StandardUnit.None)
                .withValue(1.1)
                .withDimensions(dimension);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace("SITE/TRAFFIC")
                .withMetricData(datum);

        PutMetricDataResult response = cw.putMetricData(request);
    }

}
