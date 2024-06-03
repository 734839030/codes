package main

import (
	"google.golang.org/grpc"
	"grpc/pb"
	"grpc/server/service"
	"log"
	"net"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	server := grpc.NewServer()
	pb.RegisterGreeterServer(server, &service.GreeterImpl{})
	listen, err := net.Listen("tcp", ":5100")
	if err != nil {
		log.Fatalf("listent error:%s", err)
	}
	go func() {
		if err = server.Serve(listen); err != nil {
			log.Fatalf("Serve error:%s", err)
		}
	}()
	sig := make(chan os.Signal, 1)
	signal.Notify(sig, syscall.SIGINT, syscall.SIGTERM)
	o := <-sig
	log.Printf("recieve signal %s ,server will stop gracefully", o.String())
	server.GracefulStop()
}
