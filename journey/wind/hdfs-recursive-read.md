hdfs����Ŀ¼�ݹ飺

mr����recursive

Configuration conf = new Configuration();
JobConf jobConf = new JobConf(conf,FuncPointClean.class);
//���ַ�ʽ����ʹ
//jobConf.setBoolean("mapreduce.input.fileinputformat.input.dir.recursive", true);
//ֱ��ʹ�ó���
jobConf.setBoolean(FileInputFormat.INPUT_DIR_RECURSIVE , true);
jobConf.setNumMapTasks(2);
jobConf.setNumReduceTasks(2);
jobConf.set("mapred.min.split.size", "30");
Job job =Job.getInstance(jobConf,"FuncPointClean");
