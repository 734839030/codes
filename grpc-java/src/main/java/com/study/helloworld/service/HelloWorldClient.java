package com.study.helloworld.service;

import com.google.common.util.concurrent.ListenableFuture;
import com.study.helloworld.proto.HelloWorldGrpc;
import com.study.helloworld.proto.HelloWorldGrpc.HelloWorldBlockingStub;
import com.study.helloworld.proto.HelloWorldGrpc.HelloWorldFutureStub;
import com.study.helloworld.proto.HelloWorldGrpc.HelloWorldStub;
import com.study.helloworld.proto.HelloWorldRequest;
import com.study.helloworld.proto.HelloWorldResponse;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class HelloWorldClient {

    private static String target = "localhost:50051";

    private HelloWorldBlockingStub helloWorldBlockingStub;
    private HelloWorldStub helloWorldStub;
    private HelloWorldFutureStub helloWorldFutureStub;


    public HelloWorldClient(Channel channel) {
        // 同步返回
        helloWorldBlockingStub = HelloWorldGrpc.newBlockingStub(channel);
        helloWorldBlockingStub.withDeadlineAfter(3, TimeUnit.SECONDS);
        // 异步
        helloWorldStub = HelloWorldGrpc.newStub(channel);
        // future 主动调用返回
        helloWorldFutureStub = HelloWorldGrpc.newFutureStub(channel);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        HelloWorldClient helloWorldClient = new HelloWorldClient(channel);
        helloWorldClient.say1("grpc1");
        helloWorldClient.say2("grpc2");
        helloWorldClient.say3("grpc3");

        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);

    }

    /**
     * blocking Say hello to server.
     */
    public void say1(String name) {
        System.out.println("Will try to greet " + name + " ...");
        HelloWorldRequest request = HelloWorldRequest.newBuilder().setName(name).build();
        HelloWorldResponse response;
        try {
            response = helloWorldBlockingStub.say(request);
        } catch (StatusRuntimeException e) {
            System.out.println(String.format("RPC failed: %s", e.getStatus().getDescription()));
            return;
        }
        System.out.println("Greeting: " + response.getMessage());
    }

    /**
     * async Say hello to server.
     */
    public void say2(String name) {
        System.out.println("Will try to greet " + name + " ...");
        HelloWorldRequest request = HelloWorldRequest.newBuilder().setName(name).build();
        StreamObserver<HelloWorldResponse> response = new StreamObserver<HelloWorldResponse>() {

            @Override
            public void onNext(HelloWorldResponse value) {

                System.out.println("Greeting: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("error:" + t.getMessage());
            }

            @Override
            public void onCompleted() {
            }
        };
        helloWorldStub.say(request, response);
    }

    /**
     * future Say hello to server.
     */
    public void say3(String name) throws ExecutionException, InterruptedException {
        System.out.println("Will try to greet " + name + " ...");
        HelloWorldRequest request = HelloWorldRequest.newBuilder().setName(name).build();

        ListenableFuture<HelloWorldResponse> future = helloWorldFutureStub.say(request);
        HelloWorldResponse helloWorldResponse = future.get();
        System.out.println("Greeting: " + helloWorldResponse.getMessage());
    }
}
