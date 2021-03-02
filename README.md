后端服务器上下线提醒  
会在后端服务器开服完成以及开始关服时发出全体消息  
可以使用指令发送自定义消息  
QQ消息规范：!!QQ <插件id，为“serverstatus”> <子服务器id，位于BC端配置文件中> <自定义消息>  
底层通讯格式：serverstatus.<子服务器端口>.<消息动作>.<自定义内容>.\[自定义内容\]...
## ServerStatus-BungeeCord
### 配置文件(使用[DreamYaml](https://github.com/Osiris-Team/Dream-Yaml)框架)
\# 内置服务器端口，默认端口为556  
socket-port: 556  
\# 将服务器id翻译成名称，使用如下格式：  
\# - server1: 例子1  
\# - server2: 例子2  
translate-servername:   
  \- server1: 例子1  
  \- server2: 例子2  
\# 支持颜色代码如“&6”，转义“\\&” -> “\&”，使用“${server_translation}”代表上下线的服务器  
server-started-broadcast: &6${server_translation} is online now!  
server-closed-broadcast: &6${server_translation} is offline now!  
## ServerStatus-Fabric
### 配置文件
{   
&emsp;// 连接bc的serverStatus的内置服务器ip及端口，ip默认为localhost，端口默认为556  
&emsp;"socketIp": "localhost",  
&emsp;"socketPort": 556,  
&emsp;// 是否允许4级op使用指令  
&emsp;"allowOpCommand": true,  
&emsp;// 语言(zh_cn)，默认为zh_cn  
&emsp;"lang": "zh_cn"  
}
### 使用方法
#### 自定义语言文件(自制框架试水)
## ServerStatus-Spigot
### 配置文件
\# 连接bc端本插件的内置服务器ip及端口，ip默认为localhost，端口默认为556  
socket-ip: localhost  
socket-port: 556  
