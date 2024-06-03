package service

import (
	"context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
	"grpc/pb"
	"log"
	"time"
)

type GreeterImpl struct {
	pb.UnimplementedGreeterServer
}

func (g *GreeterImpl) SayHello(ctx context.Context, request *pb.Request) (*pb.Response, error) {
	log.Printf("recieve msg:%s \n", request.Msg)
	if len(request.Msg) > 10 {
		// 使用扩展头传递错误信息
		pairs := metadata.Pairs("x-err-code", "1000")
		grpc.SendHeader(ctx, pairs)
		return nil, status.Error(codes.Internal, "msg limit 10")
	}

	return &pb.Response{
		Msg:  "you say " + request.Msg,
		Time: time.Now().UnixMilli(),
	}, nil
}
