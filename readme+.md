### 添加一个domain
1. 在`$WAYEB_HOME/cef/src/main/scala/stream/domain/`下添加一个新的domain，并且继承LineParser方法。
2. 在`$WAYEB_HOME/cef/src/main/scala/stream/StreamFactory.scala`中添加对应的domain match。同时Predicate中的变量也在这里定义了。
3. 

### 添加数据
**CSV**
1. 在`$WAYEB_HOME/data/`里添加数据文件夹和csv数据。

**KafkaStream**

### 添加Pattern
1. 在`$WAYEB_HOME/patterns/`里添加pattern。
2. 一定要记得Pattern中的数字需要以小数的形式，不能识别整数。
