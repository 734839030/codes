// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.1.0
// - protoc             v3.15.8
// source: hello_world.proto

package pb

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// HelloWorldClient is the client API for HelloWorld service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type HelloWorldClient interface {
	Say(ctx context.Context, in *HelloWorldRequest, opts ...grpc.CallOption) (*HelloWorldResponse, error)
}

type helloWorldClient struct {
	cc grpc.ClientConnInterface
}

func NewHelloWorldClient(cc grpc.ClientConnInterface) HelloWorldClient {
	return &helloWorldClient{cc}
}

func (c *helloWorldClient) Say(ctx context.Context, in *HelloWorldRequest, opts ...grpc.CallOption) (*HelloWorldResponse, error) {
	out := new(HelloWorldResponse)
	err := c.cc.Invoke(ctx, "/com.study.helloworld.proto.HelloWorld/Say", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// HelloWorldServer is the server API for HelloWorld service.
// All implementations must embed UnimplementedHelloWorldServer
// for forward compatibility
type HelloWorldServer interface {
	Say(context.Context, *HelloWorldRequest) (*HelloWorldResponse, error)
	mustEmbedUnimplementedHelloWorldServer()
}

// UnimplementedHelloWorldServer must be embedded to have forward compatible implementations.
type UnimplementedHelloWorldServer struct {
}

func (UnimplementedHelloWorldServer) Say(context.Context, *HelloWorldRequest) (*HelloWorldResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method Say not implemented")
}
func (UnimplementedHelloWorldServer) mustEmbedUnimplementedHelloWorldServer() {}

// UnsafeHelloWorldServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to HelloWorldServer will
// result in compilation errors.
type UnsafeHelloWorldServer interface {
	mustEmbedUnimplementedHelloWorldServer()
}

func RegisterHelloWorldServer(s grpc.ServiceRegistrar, srv HelloWorldServer) {
	s.RegisterService(&HelloWorld_ServiceDesc, srv)
}

func _HelloWorld_Say_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(HelloWorldRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(HelloWorldServer).Say(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/com.study.helloworld.proto.HelloWorld/Say",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(HelloWorldServer).Say(ctx, req.(*HelloWorldRequest))
	}
	return interceptor(ctx, in, info, handler)
}

// HelloWorld_ServiceDesc is the grpc.ServiceDesc for HelloWorld service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var HelloWorld_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "com.study.helloworld.proto.HelloWorld",
	HandlerType: (*HelloWorldServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "Say",
			Handler:    _HelloWorld_Say_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "hello_world.proto",
}