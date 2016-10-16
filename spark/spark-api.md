1.类似于sql的groupby，用groupbykey,结果是个

2.需要对的v进行计算：max，add等，用reducebykey

3.同前，只是多了初始值，foldByKey(zeroValue = 0,numPartitions = 2)(func = seq)

需要进行类型转换(v,v)=>(v,u),使用aggregateByKey("0")(seqOp,combOp)