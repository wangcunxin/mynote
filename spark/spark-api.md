1.������sql��groupby����groupbykey,����Ǹ�<key,list[]>

2.��Ҫ��<k,v>��v���м��㣺max��add�ȣ���reducebykey

3.ͬǰ��ֻ�Ƕ��˳�ʼֵ��foldByKey(zeroValue = 0,numPartitions = 2)(func = seq)

4. ��Ҫ��������ת��(v,v)=>(v,u),ʹ��aggregateByKey("0")(seqOp,combOp)