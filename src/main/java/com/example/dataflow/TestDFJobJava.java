package com.example.dataflow;

import com.google.cloud.spanner.Mutation;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.spanner.SpannerIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.joda.time.Duration;

import java.util.Random;

public class TestDFJobJava {

    public static void main(String[] args) {

        Pipeline pipeline = Pipeline.create(PipelineOptionsFactory.fromArgs(args).create());
        pipeline.apply("ReadStrinsFromPubsub",
                        PubsubIO.readStrings().fromSubscription("full_subscription_name"))
                .apply("window", Window.into(FixedWindows.of(Duration.standardSeconds(5))))
                .apply("CreateMutation", ParDo.of(new DoFn<String, Mutation>() {
                    @ProcessElement
                    public void processElement(@Element String word, OutputReceiver<Mutation> out) {
                        Mutation mutation= Mutation.newInsertOrUpdateBuilder("Test")
                                .set("Col1").to(new Random().nextLong())
                                .set("Col2").to(new Random().nextLong())
                                .build();
                        out.output(mutation);
                    }
                })).apply("SaveMutationToSpanner", SpannerIO.write()
                .withProjectId("project_id")
                .withInstanceId("instance")
                        .withDatabaseId("db")
                );
        pipeline.run().waitUntilFinish();
    }
}
