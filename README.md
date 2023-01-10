## 启动

1. 克隆项目：`https://github.com/zhuweixiong22/scheduling.git`。
2. 本地数据库创建一个名为 `scheduling` 的库。
3. 修改配置文件 `src/main/resources/application.yml`，主要修改数据库连接的用户名和地址。
4. 启动项目。
5. 浏览器访问 `http://localhost:8080`，可以看到如下页面：

![image-20230110164904586](https://zwx-images-1305338888.cos.ap-guangzhou.myqcloud.com/img/2023/01/10/image-20230110164904586.png)

表示启动成功。

## 功能介绍

1. 项目启动时，会自动从数据库中加载状态为 1 的定时任务并开始执行，1 表示处于开启状态的定时任务，0 表示处于禁用状态的定时任务。
2. 点击页面上的**添加作业**按钮，可以添加一个新的定时任务，新任务的 Bean 名称、方法名称以及方法参数如果和已有的记录相同，则认为是重复作业，重复作业会添加失败。

添加作业的页面如下：

![](https://zwx-images-1305338888.cos.ap-guangzhou.myqcloud.com/img/2023/01/10/20210910181820.png)

这里涉及到几个参数，含义如下：

- Bean 名称：这是项目中注入 Spring 的 Bean 名称，测试代码中以 `SchedulingTaskDemo.java` 为例。
- 方法名称：参数 1 中 bean 里边的方法名称。
- 方法参数：参数 2 中方法的参数。
- Cron 表达式：定时任务的 Cron 表达式。
- 作业状态：开启和禁用两种。开启的话，添加完成后这个定时任务就会开始执行，禁用的话，就单纯只是将记录添加到数据库中。



3. 点击作业编辑，可以修改作业的各项数据：

![](https://zwx-images-1305338888.cos.ap-guangzhou.myqcloud.com/img/2023/01/10/20210910182736.png)

修改后会立马生效。

4. 点击作业删除，可以删除一个现有的作业。假如删除的作业正在执行，则先停止该作业，然后删除。
5. 点击列表中的 switch 按钮也可以切换作业的状态。

## 技术栈

- SpringBoot
- Jpa
- MySQL
- Spring Job
- Vue
