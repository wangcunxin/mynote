hdfs读入目录递归：

mr输入recursive

Configuration conf = new Configuration();
JobConf jobConf = new JobConf(conf,FuncPointClean.class);
//这种方式不好使
//jobConf.setBoolean("mapreduce.input.fileinputformat.input.dir.recursive", true);
//直接使用常量
jobConf.setBoolean(FileInputFormat.INPUT_DIR_RECURSIVE , true);
jobConf.setNumMapTasks(2);
jobConf.setNumReduceTasks(2);
jobConf.set("mapred.min.split.size", "30");
Job job =Job.getInstance(jobConf,"FuncPointClean");
