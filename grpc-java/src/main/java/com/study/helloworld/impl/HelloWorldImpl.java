package com.study.helloworld.impl;


import com.study.helloworld.proto.HelloWorldGrpc;
import com.study.helloworld.proto.HelloWorldRequest;
import com.study.helloworld.proto.HelloWorldResponse;
import io.grpc.stub.StreamObserver;

public class HelloWorldImpl extends HelloWorldGrpc.HelloWorldImplBase {

    @Override
    public void say(HelloWorldRequest request, StreamObserver<HelloWorldResponse> responseObserver) {
        HelloWorldResponse response = HelloWorldResponse.newBuilder().setMessage("hello:" + request.getName()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
