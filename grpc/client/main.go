package main

import (
	"context"
	"fmt"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/metadata"
	"grpc/pb"
	"log"
	"time"
)

func main() {
	conn, err := grpc.NewClient("127.0.0.1:5100", grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithConnectParams(grpc.ConnectParams{MinConnectTimeout: 1 * time.Second}))
	if err != nil {
		log.Fatalf("connect error:%s\n", err)
	}
	client := pb.NewGreeterClient(conn)
	ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
	defer cancel()
	resp, err := client.SayHello(ctx, &pb.Request{Msg: "hello"})
	if err != nil {
		log.Fatalf("call error:%s\n", err)
	} else {
		log.Printf("recieve response %+v", resp)
	}
	// 接收扩展头
	var header metadata.MD
	resp, err = client.SayHello(ctx, &pb.Request{Msg: "hellohellohello"}, grpc.Header(&header))
	if err != nil {
		fmt.Println(header)
		log.Fatalf("call error:%s\n", err)
	} else {
		log.Printf("recieve response %+v", resp)
	}
}
