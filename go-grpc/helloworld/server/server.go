package main

import (
	"context"
	"go-grpc/pb"
	"log"
	"net"

	"google.golang.org/grpc"
)

const address = ":50052"

// server is used to implement helloworld.GreeterServer.
type server struct {
	pb.UnimplementedHelloWorldServer
}

func (s *server) Say(ctx context.Context, in *pb.HelloWorldRequest) (*pb.HelloWorldResponse, error) {
	log.Printf("Received: %v", in.GetName())
	return &pb.HelloWorldResponse{Message: "hello :" + in.GetName()}, nil
}

func main() {
	listen, err := net.Listen("tcp", address)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer()
	pb.RegisterHelloWorldServer(s, &server{})

	if err := s.Serve(listen); err != nil {
		log.Fatalf("failed to serve: %v", err)
	}
}
