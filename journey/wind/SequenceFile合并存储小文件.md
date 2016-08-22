public class SequenceFileUtil
{
  private static final Log log = LogFactory.getLog(SequenceFileUtil.class);
  private static FileWriter fw = null;

  private long line = 0L;

  public void writeSeqFileForZip(String srcPath, String desFile)
    throws Exception
  {
    Configuration conf = new Configuration();
    conf.set("io.seqfile.compression.type", "BLOCK");
    conf.set("io.compression.codecs", "com.hadoop.compression.lzo.LzoCodec");

    FileSystem desfs = FileSystem.get(conf);
    Path desPath = new Path(desFile);

    if (desfs.exists(desPath)) {
      desfs.delete(desPath, false);
    }

    SequenceFile.Writer writer =
      SequenceFile.createWriter(desfs, conf, desPath, LongWritable.class, Text.class);

    ZipFile zipFile = new ZipFile(srcPath);
    InputStream is = null;
    BufferedReader dr = null;

    Enumeration entries = zipFile.entries();

    while (entries.hasMoreElements()) {
      ZipEntry entry = (ZipEntry)entries.nextElement();
      is = zipFile.getInputStream(entry);
      dr = new BufferedReader(new InputStreamReader(is));
      String record = null;
      LongWritable idKey = new LongWritable(0L);
      while ((record = dr.readLine()) != null)
        if (!record.trim().equalsIgnoreCase(""))
        {
          idKey.set(this.line);
          Text recordText = new Text();
          recordText.set(record);
          writer.append(idKey, recordText);
          this.line += 1L;
        }
      dr.close();
    }

    zipFile.close();
    IOUtils.closeStream(writer);
    desfs.close();
  }
