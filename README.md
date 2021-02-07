后端服务器上下线提醒  
会在后端服务器开服完成以及开始关服时发出全体消息  
可以使用指令发送自定义消息
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
## ServerStatus-Spigot
