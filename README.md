# 在mybatis-plus上裸奔的万能统计查询接口

### 主要功能

* 基于javascript的sql模板，支持存储过程，数据源切换，java方法调用。
* 基于mybatis-plus，支持运行裸写sql，给您多一种选择。
* 结合上述两个功能提供了万能http接口，让你不用写java代码也能完成老板的需求

这个万能接口非常适合统计类的需求，统计需求的特点是：需求多变，实际需求开发过程中往往发现对象设计在该场景下非常鸡肋，因为涉及到的对象都是vo，不是entity以及其它领域对象。

如果基于mybatis xml，在典型应用场景下，一个统计需求需要新增一个http接口（如果你用spring的话，可能需要新增一个controller，service），实际的查询需要一个或多个sql统计查询语句（在mybatis中，
要为每个语句新增一个mapper方法，每个mapper方法需要新增输入输出的对象映射，每个语句的sql又要在写在xml中）。
要是需求变了，上面的一连串工作都可能需要修改，费事费力，令人郁闷。

而使用本项目的方法，接口和对象省了，多个sql以存储过程的形式写在这个js里，而且所写即所见所见即所得，省去了前后端联调，发布。

没错，一行java代码都不需要写。

### 使用示例

示例模板：

```

/* 正式开始模板 */
/* 支持注释，语法高亮，便于检视sql的正确性 */
/* 变量可以通过http参数传入 */
var param1 = get("param1");

/* 支持存储过程 因此不用统计的sql写在一句sql里面，可以拆成多个sql */
/* 支持动态数据源切换 下面指定查询数据源为zhcx1 */
/* var date1  = $<stat1::SELECT DATE_FORMAT(DATE_ADD(NOW(),INTERVAL -1 DAY),'%Y%m%d');>; */

/* 支持内嵌函数 可在此js模板中调用项目中的java函数 */
var id = guid();
$<stat1::INSERT INTO tbl_test (id,remark) values ('{{id}}', '{{id}}');>;

/* demo */
var result = $<stat1::SELECT * from tbl_test where id > '{{param1}}';>;
/* 返回结果 */
return result;
```



万能接口：

![万能接口](https://dlx-export.oss-cn-hangzhou.aliyuncs.com/690546270128832512_企业微信截图_15846803761538.png)

如上图所示，前端只需传入模板标识以及模板绑定的参数（比如从UI控件中得到），然后请求到这个万能接口即可。

TODO：对于sql模板，可以做一个restfull服务实现模板的管理；后续基于万能统计接口增加防止sql注入相关的功能。

### 如何运行

```
mvn clean install
```

按照一般操作运行springboot项目即可。