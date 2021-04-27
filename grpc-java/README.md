### 插件自动生成代码

https://grpc.io/docs/languages/java/generated-code/

### 手动生成

```
# proto_path 必须，且不能和proto 同目录
protoc --proto_path=../ --cpp_out=DST_DIR --java_out=DST_DIR --python_out=DST_DIR --go_out=DST_DIR --ruby_out=DST_DIR --objc_out=DST_DIR --csharp_out=DST_DIR proto/*

```
