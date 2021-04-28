### 插件自动生成代码

https://grpc.io/docs/languages/java/generated-code/

### 手动生成

需要安装java grpc 插件

```
再proto 目录执行
protoc --java_out=../java --grpc-java_out=../java  --plugin=protoc-gen-grpc-java=插件路径  **/*.proto 
```
