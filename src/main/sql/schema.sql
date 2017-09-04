if exists (select 1
            from  sysobjects
           where  id = object_id('seckill')
            and   type = 'U')
   drop table seckill
go

if exists (select 1
            from  sysobjects
           where  id = object_id('success_killed')
            and   type = 'U')
   drop table success_killed
go

create table seckill (
   seckill_id           bigint               not null     identity(1,1),
   name                 varchar(20)          not null,
   number               int					        not null,
   start_time           datetime            not null,
   end_time             datetime            not null,
   create_time          datetime            not null    DEFAULT getDate(),
   constraint PK_SECKILL primary key nonclustered (seckill_id),
)
go

create table success_killed (
   seckill_id           bigint               not null,
   user_phone           bigint               not null,
   state                smallint             not null      DEFAULT -1, --  '-1':无效，'0':成功,'1':已付款,'2':已发货
   create_time          datetime            not null      DEFAULT getDate(),
   --使用联合主键形式
   constraint PK_SUCCESS_KILLED primary key nonclustered (seckill_id, user_phone),
)
go

INSERT INTO "seckill"(name,number,start_time,end_time) VALUES ('100元秒杀iphone',100,'2017-08-17 00:00:00', '2017-08-18 00:00:00');
INSERT INTO "seckill"(name,number,start_time,end_time) VALUES ('200元秒杀ipad',200,'2017-08-17 00:00:00', '2017-08-18 00:00:00');
INSERT INTO "seckill"(name,number,start_time,end_time) VALUES ('3000元秒杀iwatch',300,'2017-08-17 00:00:00', '2017-08-18 00:00:00');
INSERT INTO "seckill"(name,number,start_time,end_time) VALUES ('400元秒杀macbook',500,'2017-08-17 00:00:00', '2017-08-18 00:00:00');

