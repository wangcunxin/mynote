//位运算的运用:压缩和解压缩
public class DicTool {

    
    public static void compress(int pos, byte[] bArr) {
        int posInArr = pos/8;//定位到数组的位置
        int posInCell = pos%8;//定位到一个单元格的左移位置
        bArr[posInArr] = (byte)(bArr[posInArr]| (1 << posInCell));//加上原值
    }
    
    public static List deCompress(byte[] bArr) {
        List resList = new ArrayList();
        int bLen = bArr.length;        
        long bCount = 0;
        for(int i=0;i
            byte b = bArr[i];
            bCount = i*8;
            int bitCount = 0;
            for(int j=0;j<8;j++) {
                int flag = (1<<j)& b;
                bitCount++;
                if(flag != 0) {
                    resList.add(bCount+bitCount-1);
                }
            }
        }
        return resList;
    }
    
    public static byte[] doJoint(byte[] prevbArr, byte[] tailbArr) {
        int len = prevbArr.length;//2

        for(int i=0;i
            prevbArr[i] &= tailbArr[i];//i=0
        }
        return prevbArr;
    }
    
    public static byte[] doUnion(byte[] prevbArr, byte[] tailbArr) {
        int len = prevbArr.length;

        for(int i=0;i
            prevbArr[i] |= tailbArr[i];
        }
        return prevbArr;
    }
   
}
