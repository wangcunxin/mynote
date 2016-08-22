hbase 备份
export:
hbase org.apache.hadoop.hbase.mapreduce.Export trades /tmp/export_hbase

import:
hbase org.apache.hadoop.hbase.mapreduce.Import trades export_hbase
