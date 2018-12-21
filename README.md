# sm_mall
实际项目开发用的
 spring+sprigmvc+netty4+resteasy+shiro+j2cache+jetty
    
    
    1  项目通过内嵌jetty的生成可执行jar项目。如果需要war包（部署到tomcat项目中），可以在某个web模块mvn war:war。即可生成生成的war包和jar包都可使用
    2  netty4+resteasy整合restful接口.
     
     注意 ，mvn war:war的时候，总目标必须mvn clean install
   

一:  模块sm_adminweb ,后台如果需要war包，可使用mvn war:war install，生成的war包和jar包都可使用。war可直接放到支持j2ee容器，比如tomcat



二 core模块
   --注解---
   @ShowLogAnnotation 记录操作路径到数据库中，以及显示请求数据返回数据
        1 value  操作路径描述
        2 isSaveDb  是否需要把用户的操作路径保存到数据库里面
         
   @CheckResubmitAnnotation  防止重复提交
	  long expireTime() default 2L              重复提交超时时间 单位：秒
	  String token() default “token字符串”       用户token,用来做唯一标识
	  

三 一个在开发节省时间的工具Jrebel,本地修改东西，不要重启启动
如下连接，你懂得
https://blog.csdn.net/xingbaozhen1210/article/details/81093041	  
	  
	
https://blog.csdn.net/a4475686/article/details/79378000  mybatit修改热部署

	
