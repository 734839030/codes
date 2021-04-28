# proto 不包含go相关的
# go get google.golang.org/protobuf/cmd/protoc-gen-go
# go get google.golang.org/grpc/cmd/protoc-gen-go-grpc
# go的生成目录已go_out go-grpc_out 为目录，然后读取go_package 最终得到路径
protoc --go_out=. --go-grpc_out=.  **/*.proto
