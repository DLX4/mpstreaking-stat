/* 变量可以通过http参数传入 这里为了方便手动set*/
/* set("ksbjsj", "2020-01-01 14:53:44.0"); */
/* set("jsbjsj", "2020-05-01 14:53:44.0"); */
/* 正式开始模板 */
/* 支持注释，语法高亮，便于检视sql的正确性 */
var param1 = get("param1");
/* var jsbjsj = get("jsbjsj"); */

/* 支持存储过程 因此不用统计的sql写在一句sql里面，可以拆成多个sql */
/* 支持动态数据源切换 下面指定查询数据源为zhcx1 */
/* var date1  = $<stat1::SELECT DATE_FORMAT(DATE_ADD(NOW(),INTERVAL -1 DAY),'%Y%m%d');>; */

/* 支持内嵌函数 可在此js模板中调用项目中的java函数 */
var id = guid();
$ < stat1::INSERT
INTO
tbl_test(id, remark)
values('{{id}}', '{{id}}');
>
;

/* demo */
var result = $ < stat1::SELECT * from
tbl_test
where
id > '{{param1}}';
>
;
/* 返回结果 */
return result;
