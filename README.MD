## MAVEN版本控制
可以使用下面的命令更新MAVEN版本:
```shell
 mvn versions:set -DnewVersion=0.0.2-SNAPSHOT  
```
因为我关闭了备份参数,所以不会生成备份文件,
更多操作,参考文档:[versions-maven-plugin](https://www.mojohaus.org/versions-maven-plugin/usage.html)
