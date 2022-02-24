社区版下载地址

> https://downloads.mysql.com/archives/community/

# Mysql的两种升级方式

## 就地升级（In-place Upgrade）

​	适用于小版本升级使用，5.7.32->5.7.37

​	关闭旧版本mysql，用新的替换旧的二进制文件或软件包，在现有数据目录上重启数据库，执行mysql_upgrade

## 逻辑升级（Logical Upgrade）

​	适用于大版本升级使用，如5.5->5.7

​	使用备份或导出实用程序（如mysqldump，Xtrabackup）从旧mysql实例导出SQL ，安装新的mysql数据库版本，再将SQL应用于新的mysql实例。

## 官方支持的升级路径

> - 同一个大版本中的小版本升级，比如5.7.25到5.7.28。
> - 跨版本升级，但只支持跨一个版本升级，比如5.5到5.6，5.6到5.7。
> - 不支持跨版本的直接升级，比如直接从5.5到5.7，可以先从5.5升级到5.6，再从5.6升级到5.7。

# 前期环境了解

## 查询当前数据库大小

```sql
SELECT table_schema,SUM(data_length+index_length)/1024/1024 AS total_mb,SUM(data_length)/1024/1024 AS data_mb,SUM(index_length)/1024/1024 AS index_mb, SUM(data_free)/1024/1024 AS free_mb,COUNT(*) AS tables_num,CURDATE() AS today FROM information_schema.tables where table_schema not in ('mysql','sys','information_schema','performance_schema') GROUP BY table_schema ORDER BY total_mb desc
```

![image-20220222105332242](D:\system\custom_code\demo-jdk\src\resources\note\数据库\Mysql\image\image-20220222105332242.png)

## mysqldump:快速备份数据库

> mysqldump使用说明：https://www.cnblogs.com/chenmh/p/5300370.html
>
> mysqldump导入导出总结说明：https://www.cnblogs.com/sucretan2010/p/11406619.html

## 查看mysql 当前版本

> 执行此命令前，需要确保mysql安装路径已经加入到环境变量

```sql
mysql -V
```

<img src="C:\Users\yhmsi\AppData\Roaming\Typora\typora-user-images\image-20220222142059615.png" alt="image-20220222142059615" style="zoom:150%;" align="left" />

# 就地升级过程

> 只升级小版本 如5.7.32 ->5.7.37

## 1、检查XA事务

  如果确保用户已经全部停止操作后才进行更新，可以不进行此步骤

```sql
# XA检查是否有值
mysql> XA RECOVER;
Empty set (0.00 sec)

# 若有值，则需要 COMMIT 或 ROLLBACK xid
mysql> XA COMMIT xid;
或
mysql> XA ROLLBACK xid;
```

## 2、备份

```sql
# 导出全部数据库 包括mysql自带的库也会导出， -uroot ,root 是用户名，--single-transaction --hex-blob与Bolb等二进制字段有关
mysqldump  -uroot -p --single-transaction --hex-blob --all-databases --events > /data/mysql.sql

# 导出指定的数据库
mysqldump -uroot -p  --single-transaction --hex-blob --databases rcm_test > /data/mysql_test.sql

# 导出指定的多个数据库
mysqldump -uroot -p  --single-transaction --hex-blob --databases db1 db2 > /data/mysql.sql
```

## 3、完全关闭MySQL

```sql
#  登录服务器Mysql客户端，执行
mysql> SET GLOBAL innodb_fast_shutdown=0;
Query OK, 0 rows affected (0.00 sec)
# 退出
mysql> exit
Bye
# 关闭mysql服务
[root@BDGatewayT ~]# service mysql stop
Shutting down MySQL.... SUCCESS! 
```

> innodb_fast_shutdown参数说明：https://blog.csdn.net/edyf123/article/details/81026139



## 4.升级 MySQL 二进制安装或软件包

 上传对应版本的Mysql压缩包,本案例是从5.7.32 -> 5.7.37

<img src="D:\system\custom_code\demo-jdk\src\resources\note\数据库\Mysql\image\image-20220222133603250.png" alt="image-20220222133603250" style="zoom:100%;" align="left" />

### 4.1 、覆盖前备份

> 备份当前安装目录下的 mysql.server,需要记住其中的
>
> basedir=/data/mysql/mysql-5.7.32-linux-glibc2.12-x86_64
> datadir=/data/mysql/mysql-5.7.32-linux-glibc2.12-x86_64/data

```shell
cp /data/mysql/mysql-5.7.37-linux-glibc2.12-x86_64/support-files/mysql.server  /data/mysql
```



### 4.2、解压二进制安装包，覆盖原来的安装路径：

> 覆盖是会询问是否覆盖，直接回车就好，需要回车几十下

```powershell
tar -zxvf mysql-5.7.37-linux-glibc2.12-x86_64.tar.gz
# 复制/data/mysql/mysql-5.7.37-linux-glibc2.12-x86_64/路径下的所有文件，覆盖到/data/mysql/mysql-5.7.32-linux-glibc2.12-x86_64/
# 开头的\不能去掉，否则不能强制覆盖
\cp -frp /data/mysql/mysql-5.7.37-linux-glibc2.12-x86_64/* /data/mysql/mysql-5.7.32-linux-glibc2.12-x86_64/
```

### 4.3、还原属性

 	复制备份的【mysql.server】文件中的basedir、datadir属性到【mysql安装目录】/support-files/mysql.server文件中

<img src="C:\Users\yhmsi\AppData\Roaming\Typora\typora-user-images\image-20220222141449462.png" alt="image-20220222141449462" style="zoom:150%;" />

### 4.4、在现有目录下启动mysql

```
cd /data/mysql/mysql-5.7.32-linux-glibc2.12-x86_64/support-files
su mysql
./mysql.server start
```

### 4.5、执行升级命令

> ```shell
> mysql_upgrade -u root -p
> 
> [root@db01 bin]# mysql_upgrade -uroot -p
> Enter password:
> Checking if update is needed.
> Checking server version.
> Running queries to upgrade MySQL server.
> Checking system database.
> mysql.columns_priv                                 OK
> mysql.db                                           OK
> mysql.engine_cost                                  OK
> mysql.event                                        OK
> mysql.func                                         OK
> mysql.general_log                                  OK
> mysql.gtid_executed                                OK
> mysql.help_category                                OK
> mysql.help_keyword                                 OK
> mysql.help_relation                                OK
> mysql.help_topic                                   OK
> mysql.innodb_index_stats                           OK
> mysql.innodb_table_stats                           OK
> mysql.ndb_binlog_index                             OK
> mysql.plugin                                       OK
> mysql.proc                                         OK
> mysql.procs_priv                                   OK
> mysql.proxies_priv                                 OK
> mysql.server_cost                                  OK
> mysql.servers                                      OK
> mysql.slave_master_info                            OK
> mysql.slave_relay_log_info                         OK
> mysql.slave_worker_info                            OK
> mysql.slow_log                                     OK
> mysql.tables_priv                                  OK
> mysql.time_zone                                    OK
> mysql.time_zone_leap_second                        OK
> mysql.time_zone_name                               OK
> mysql.time_zone_transition                         OK
> mysql.time_zone_transition_type                    OK
> mysql.user                                         OK
> The sys schema is already up to date (version 1.5.2).
> Checking databases.
> new_data.auth_group                                OK
> new_data.auth_group_permissions                    OK
> new_data.auth_permission                           OK
> new_data.cate                                      OK
> new_data.collection                                OK
> new_data.comment                                   OK
> new_data.django_admin_log                          OK
> new_data.django_content_type                       OK
> new_data.django_migrations                         OK
> new_data.django_session                            OK
> new_data.new                                       OK
> new_data.new_correlation                           OK
> new_data.tb_users                                  OK
> new_data.tb_users_groups                           OK
> new_data.tb_users_user_permissions                 OK
> sys.sys_config                                     OK
> Upgrade process completed successfully.
> Checking if update is needed.
> ```

## 5、关闭mysql重新启动

```shell
# 停止服务：
service mysql stop
# 启动服务：
service mysql start
```

## 6、mysql查询版本

>  mysql  -V

<img src="C:\Users\yhmsi\AppData\Roaming\Typora\typora-user-images\image-20220222142059615.png" alt="image-20220222142059615" style="zoom:150%;" align="left" />

​	至此，如果查询的版本号为自己需要升级到的版本，则说明已经升级完成，接下来可以通过Navicat进行连接查询，看数据库是否正常，通过启动系统查看系统是否可以正常使用。如果出现问题，无法正常使用，则安装正常方案安装，使用mysqldump备份的数据库进行还原。

#  升级错误应急方案

 ## 1、正常安装新版本

## 2、通过备份文件还原数据库

> 导入命令：mysql -uroot -proot -h127.0.0.1 -P3306 education<d:/database.sql

或者看【前期环境了解】中的mysqldump部分



参考地址：

https://blog.csdn.net/memory6364/article/details/87169889

https://www.jianshu.com/p/add42fea081c

http://www.4k8k.xyz/article/qq_42768234/110136036

https://www.1024sou.com/article/238910.html

https://cloud.tencent.com/developer/article/1676853

https://blog.csdn.net/weixin_43715183/article/details/103714851