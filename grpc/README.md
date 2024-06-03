# Prerequisites

- `proto`编译工具`protoc`
https://github.com/protocolbuffers/protobuf/releases/
- `proto`生成`grpc`所需代码
```go
go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest
```

# 