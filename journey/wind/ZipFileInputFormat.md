public class ZipFileInputFormat extends FileInputFormat
{
   
    private static boolean isLenient = false;
    
   
    @Override
    protected boolean isSplitable( JobContext context, Path filename )
    {
        return false;
    }

   
    @Override
    public RecordReader createRecordReader( InputSplit split, TaskAttemptContext context )
        throws IOException, InterruptedException
    {
        return new ZipFileRecordReader();
    }
    
   
    public static void setLenient( boolean lenient )
    {
        isLenient = lenient;
    }
    
    public static boolean getLenient()
    {
        return isLenient;
    }
}

public class ZipFileRecordReader
    extends RecordReader
{
   
    private FSDataInputStream fsin;

   
    private ZipInputStream zip;

   
    private Text currentKey;

   
    private BytesWritable currentValue;

   
    private boolean isFinished = false;

   
    @Override
    public void initialize( InputSplit inputSplit, TaskAttemptContext taskAttemptContext )
        throws IOException, InterruptedException
    {
        FileSplit split = (FileSplit) inputSplit;
        Configuration conf = taskAttemptContext.getConfiguration();
        Path path = split.getPath();
        FileSystem fs = path.getFileSystem( conf );
       
        // Open the stream
        fsin = fs.open( path );
        zip = new ZipInputStream( fsin );
    }

   
    @Override
    public boolean nextKeyValue()
        throws IOException, InterruptedException
    {
        ZipEntry entry = null;
        try
        {
            entry = zip.getNextEntry();
        }
        catch ( ZipException e )
        {
            if ( ZipFileInputFormat.getLenient() == false )
                throw e;
        }
       
        // Sanity check
        if ( entry == null )
        {
            isFinished = true;
            return false;
        }
       
        // Filename
        currentKey = new Text( entry.getName() );
       
        // Read the file contents
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] temp = new byte[8192];
        while ( true )
        {
            int bytesRead = 0;
            try
            {
                bytesRead = zip.read( temp, 0, 8192 );
            }
            catch ( EOFException e )
            {
                if ( ZipFileInputFormat.getLenient() == false )
                    throw e;
                return false;
            }
            if ( bytesRead > 0 )
                bos.write( temp, 0, bytesRead );
            else
                break;
        }
        zip.closeEntry();
       
        // Uncompressed contents
        currentValue = new BytesWritable( bos.toByteArray() );
        return true;
    }

   
    @Override
    public float getProgress()
        throws IOException, InterruptedException
    {
        return isFinished ? 1 : 0;
    }

   
    @Override
    public Text getCurrentKey()
        throws IOException, InterruptedException
    {
        return currentKey;
    }

   
    @Override
    public BytesWritable getCurrentValue()
        throws IOException, InterruptedException
    {
        return currentValue;
    }

   
    @Override
    public void close()
        throws IOException
    {
        try { zip.close(); } catch ( Exception ignore ) { }
        try { fsin.close(); } catch ( Exception ignore ) { }
    }
}