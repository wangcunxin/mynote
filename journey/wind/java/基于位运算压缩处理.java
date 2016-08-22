public class CompressHelper2 {

    public int repeateBits = 3;
    public int repeateMax = 16777215;
    public int unitBits = 7;
    public double compressRate = 0;
    public long timeSpend = 0;

    public byte[] compress(long[] srcList) {
        if (srcList == null || srcList.length <= 0) {
            return null;
        }
        long startMili = System.currentTimeMillis();// 当前时间对应的毫秒数
        
        ArrayList result = new ArrayList();
        CommonFunc.writeHeader(result, srcList.length);

        long curSrc; // 当前要计算的数据
        long tailLength = 0;
        byte tailByte = 0;
        boolean canCompress = false;
        long curIndex = 0; // 记录当前压缩进去的表示的最大值是多少？
        long pos;
        for (int i = 0; i < srcList.length; i++) {
            curSrc = srcList[i];
            pos = curSrc - curIndex;
            // 如果此時有尾巴 或不让压，就先写入一个字节到队列中，变成 没有尾巴 和可以压缩
            if (tailLength > 0 || !canCompress) {
                // 添加的太短了，压入一个原始字节，附带tail。然后就返回了
                if (0 <= pos && pos <= unitBits) {
                    if (pos == unitBits) {
                        result.add(tailByte);
                        curIndex += unitBits;
                        canCompress = true;
                        tailByte = (byte) 1;
                        tailLength = 1;
                    } else {
                        tailByte = pushToTail(tailByte, tailLength, pos);
                        tailLength = curSrc - curIndex;
                    }
                    continue;
                }
                // 此时将tailByte压入队列即可
                else {
                    result.add(tailByte);
                    tailByte = (byte) 0;
                    tailLength = 0;
                    curIndex += unitBits;
                    canCompress = true;
                }
            }

            pos = curSrc - curIndex;

            // 此时尾巴已经处理完，也是可以压缩的，从开始进行处理
            // 先尝试
            if (pos >= unitBits * repeateBits) {
                result.set(result.size() - 1,
                        SetHighBitToOne(result.get(result.size() - 1), 7));
                long mod = pos / unitBits;
                // 将前面的0压缩进去
                writeZero(result, mod);
                curIndex += mod * unitBits;
                canCompress = false;
                pos = curSrc - curIndex;
            }

            // 尝试压缩完成后，就是单独的压缩了,先进行字节压缩
            while (pos > unitBits) {
                curIndex += unitBits;
                result.add((byte) 0);
                pos -= unitBits;
                canCompress = true;
            }

            if (pos == unitBits) {
                result.add((byte) 0);
                curIndex += unitBits;
                canCompress = true;
                tailByte = (byte) 1;
                tailLength = 1;
                continue;
            }

            // 剩下的就是尾巴了
            tailByte = getByteByPos(pos);
            tailLength = pos;

        }

        if (tailLength >= 0) {
            result.add(tailByte);
        }
        
        byte[] firstList = skewingSave(srcList, srcList[srcList.length - 1]);
        long compressLength1 = firstList.length;
        compressRate = ((result.size() - 8) * 1.0 / compressLength1);

        byte[] result2 = CommonFunc.byteArrayConventor(result);

        long endMili = System.currentTimeMillis();
        timeSpend = endMili - startMili;
        
        return result2;
    }

    // posValue 要相对于byte的起点的偏移量，从0开始计数
    private byte pushToTail(byte tailByte, long tailLength, long posValue) {
        return (byte) (tailByte | (1 << posValue));
    }

    private byte SetHighBitToOne(byte src, int bitPos) {
        return (byte) (src | (0x01 << bitPos));
    }

    private byte getByteByPos(long pos) {
        return (byte) (1 << pos);
    }

    private void writeMaxZero(ArrayList result) {
        byte data = -127;
        for (int i = 0; i < repeateBits; i++) {
            result.add(data);
        }

    }

    private long writeZero(ArrayList result, long length) {
        long writeLength = 0;
        // 表示3个字节都放不下了，现在只能放三个字节
        while (length > repeateMax) {
            writeMaxZero(result);
            length -= repeateMax;
            writeLength += repeateMax;
        }
        // 注意，這個高位在后方，地位在前面
        for (int i = 0; i < repeateBits; i++) {
            result.add((byte) (length >> (8 * i)));
        }

        return writeLength;
    }

    private long getRepeateTimes(byte[] src, int pos) {
        long result = 0;
        for (int i = 0; i < repeateBits; i++) {
            result += Math.pow(src[pos + i], i);
        }

        return result;
    }

    private byte[] skewingSave(long[] srcList, long max) {
        if (srcList == null || srcList.length <= 0) {
            return null;
        }

        int srcLength = srcList.length;
        int firstListLength = (int) ((max) / 8 + 1);// 因为从0开始计数，所以max没有减1
        byte[] firstList = new byte[firstListLength];

        int posInArr, posInCell;
        long curDeal;
        for (int i = 0; i < srcLength; i++) {
            curDeal = srcList[i];
            posInArr = (int) (curDeal / 8);
            posInCell = (int) (curDeal % 8);
            firstList[posInArr] = (byte) (firstList[posInArr] | (1 << posInCell));
        }
        return firstList;
    }

    private byte[] byteArrayConventor(ArrayList src) {
        int length = src.size();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = src.get(i);
        }

        return result;
    }
}

public class DecompressHelper2 {

    public static long[] decompress(byte[] src) throws Exception {
        ArrayList result = new ArrayList();

        byte curByte;
        boolean isCompress = false;
        long curIndex = 0;
        long length = CommonFunc.readHeader(src, 0);
        for (int i = ConstDef.HEADER_LENGTH; i < src.length; i++) {
            curByte = src[i];
            if (isCompress) {
                curIndex += CommonFunc.getRepeateTimes(src, i);
                i += ConstDef.repeateBits - 1;
                isCompress = false;
            } else {
                // 快速判断
                if (curByte != 0 && curByte != 127) {
                    for (int j = 0; j < ConstDef.unitBits; j++) {
                        if (((curByte >> j) & 0x1) == 1) {
                            result.add(curIndex + j);
                        }
                    }
                }
                curIndex += ConstDef.unitBits;
                isCompress = CommonFunc.getHighBitTag(curByte);
            }
        }
        if (length != result.size()) {
            throw new Exception("Decompress length check error!");
        }
        return CommonFunc.longArrayConventor(result);
    }

    public static byte[] decompressToByte(byte[] src) {
        byte[] result = new byte[ConstDef.maxbufferSize];
        byte curByte;
        boolean isCompress = false;
        long curIndex = 0;
        int bytePos, bitPos;

        for (int i = ConstDef.HEADER_LENGTH; i < src.length; i++) {
            curByte = src[i];
            if (isCompress) {
                curIndex += CommonFunc.getRepeateTimes(src, i);
                i += ConstDef.repeateBits - 1;
                isCompress = false;
            } else {
                // 快速判断
                if (curByte != 0 && curByte != -127) {
                    for (int j = 0; j < ConstDef.unitBits; j++) {
                        if (((curByte >> j) & 0x1) == 1) {
                            bytePos = (int) ((curIndex + j) / 8);
                            bitPos = (int) ((curIndex + j) % 8);
                            result[bytePos] = (byte) (result[bytePos] | (0x1 << bitPos));
                        }
                    }
                }
                curIndex += ConstDef.unitBits;
                isCompress = CommonFunc.getHighBitTag(curByte);
            }
        }
        return result;
    }
}

public class SetOperation {
    public byte[] union(byte[] srcA, boolean compressTagA, byte[] srcB,
            boolean compressTagB) {
        if (compressTagA && compressTagB) {
            return unoinBothCompress(srcA, srcB);
        }

        if ((!compressTagA) && (!compressTagB)) {
            DicTool.doUnion(srcA, srcB);
            return srcB;
        }

        if (compressTagB && (!compressTagA)) {
            byte[] srcTempC = srcA;
            srcA = srcB;
            srcB = srcTempC;
        }

        unionHalfCompress(srcA, srcB);
        return srcB;
    }

    private byte[] unionHalfCompress(byte[] srcA, byte[] srcB) {
        byte curByte;
        boolean isCompress = false;
        long curIndex = 0;

        int repeateTimes;
        for (int i = ConstDef.HEADER_LENGTH; i < srcA.length; i++) {
            curByte = srcA[i];
            if (isCompress) {
                repeateTimes = (int) CommonFunc.getRepeateTimes(srcA, i);
                // 因为是并集操作，A是0，则将B保留即可；
                curIndex += repeateTimes;
                i += ConstDef.repeateBits - 1;
                isCompress = false;
            } else {
                // 快速判断
                if (curByte != 0 && curByte != 127) {
                    for (int j = 0; j < ConstDef.unitBits; j++) {
                        // 因为是并集操作
                        CommonFunc.setUnoinBitForDecompressData(srcB,
                                curIndex + j, ((curByte >> j) & 0x1));
                    }
                }
                curIndex += ConstDef.unitBits;
                isCompress = CommonFunc.getHighBitTag(curByte);
            }
        }

        return srcB;
    }

    private byte[] unoinBothCompress(byte[] srcA, byte[] srcB) {
        byte[] depressSrcB = DecompressHelper2.decompressToByte(srcB);
        unionHalfCompress(srcA, depressSrcB);
        return depressSrcB;
    }

    public byte[] intersect(byte[] srcA, boolean compressTagA, byte[] srcB,
            boolean compressTagB) {
        if (compressTagA && compressTagB) {
            return joinBothCompress(srcA, srcB);
        }

        if ((!compressTagA) && (!compressTagB)) {
            DicTool.doJoint(srcA, srcB);
            return srcB;
        }

        if (compressTagB && (!compressTagA)) {
            byte[] srcTempC = srcA;
            srcA = srcB;
            srcB = srcTempC;
        }
        intersectHalfCompress(srcA, srcB);
        return srcB;
    }

    // A 是压缩的，B是没有压缩的,srcB中放置了最大长度的Byte[]
    private byte[] intersectHalfCompress(byte[] srcA, byte[] srcB) {
        byte curByte;
        boolean isCompress = false;
        long curIndex = 0;

        int repeateTimes;
        for (int i = ConstDef.HEADER_LENGTH; i < srcA.length; i++) {
            curByte = srcA[i];
            if (isCompress) {
                repeateTimes = (int) CommonFunc.getRepeateTimes(srcA, i);
                for (int j = 0; j < repeateTimes; j++) {
                    // 因为是交集操作，A是0，则将B置为0即可；
                    CommonFunc.setIntersectBitForDecompressData(srcB, curIndex
                            + j, 0);
                }
                curIndex += repeateTimes;
                i += ConstDef.repeateBits - 1;
                isCompress = false;
            } else {
                // 快速判断
                if (curByte != 0 && curByte != 127) {
                    for (int j = 0; j < ConstDef.unitBits; j++) {
                        // 因为是交集操作
                        CommonFunc.setIntersectBitForDecompressData(srcB,
                                curIndex + j, ((curByte >> j) & 0x1));
                    }
                }
                curIndex += ConstDef.unitBits;
                isCompress = CommonFunc.getHighBitTag(curByte);
            }
        }

        // 将后面的尾巴全部清空
        clearTail(srcB, curIndex);

        return srcB;
    }

    private void clearTail(byte[] src, long curIndex) {
        int bytePos = (int) (curIndex / 8);
        int bitPos = (int) (curIndex % 8);
        for (int i = bitPos; i < 8; i++) {
            CommonFunc.setIntersectBitForDecompressData(src, bytePos, 0);
        }

        for (int i = bytePos + 1; i < src.length; i++) {
            src[i] = 0;
        }
    }

    private byte[] joinBothCompress(byte[] srcA, byte[] srcB) {
        byte[] depressSrcB = DecompressHelper2.decompressToByte(srcB);
        intersectHalfCompress(srcA, depressSrcB);
        return depressSrcB;
    }
}
########
public class CommonFunc {
    public static byte[] byteArrayConventor(ArrayList src) {
        int length = src.size();
        byte[] result = new byte[length];
        for (int i = 0; i < length; i++) {
            result[i] = src.get(i);
        }

        return result;
    }

    public static byte setByteByBit(byte tar, int tarBits, byte src, int srcBits) {
        int temp = ((src >> srcBits) & 0x1);
        if (temp == 1) {
            return (byte) (tar | 1 << tarBits);
        } else {
            return (byte) (~(0 << tarBits) & tar);
        }
    }

    public static byte getRepeateByte(long repeateTimes, int repeateData) {
        byte result = (byte) 0;
        if (repeateData == 0) {
            return result;
        } else if (repeateData == 1) {
            for (int i = 0; i < repeateTimes; i++) {
                result = (byte) (result | (1 << i));
            }
        }
        return result;
    }

    public static void printByte(byte b, String header) {
        System.out.println(header + (byte) ((b >> 7) & 0x1)
                + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
                + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1)
                + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
                + (byte) ((b >> 0) & 0x1));
    }

    public static void printBytes(byte[] b, int pos) {
        if (pos >= b.length) {
            return;
        }
        for (int i = pos; i < b.length; i++) {
            printByte(b[i], i + ":");
        }
        System.out.println("\n");
    }

    public static void writeHeader(ArrayList result, long data) {
        if (data < 0) {
            return;
        }

        result.add((byte) (data >>> 0));
        result.add((byte) (data >>> 8));
        result.add((byte) (data >>> 16));
        result.add((byte) (data >>> 24));
        result.add((byte) (data >>> 32));
        result.add((byte) (data >>> 40));
        result.add((byte) (data >>> 48));
        result.add((byte) (data >>> 56));
    }

    public static long readHeader(byte[] src, int offset) {
        if (src.length < 8) {
            return 0;
        }
        long result = 0;
        for (int i = 0; i < ConstDef.HEADER_LENGTH; i++) {
            result += (src[offset + i] & 0xff) * Math.pow(256, i);
        }
        return result;
    }

    public static long getRepeateTimes(byte[] src, int pos) {
        long result = 0;
        for (int i = 0; i < ConstDef.repeateBits; i++) {
            result += (src[pos + i] & 0xff) * Math.pow(256, i);
        }
        return result * ConstDef.unitBits;
    }

    public static void setBitForDecompressData(byte[] src, long bitIndex,
            int bitValue) {
        int srcLength = src.length;
        int bytePos = (int) (bitIndex / 8);
        if (bytePos + 1 > srcLength) {
            return;
        }
        int bitPos = (int) (bitIndex % 8);
        src[bytePos + 1] = (byte) (src[bytePos + 1] | (0x1 << bitPos));
        if (bitValue == 1) {
            src[bytePos + 1] = (byte) (src[bytePos + 1] | 1 << bitPos);
        } else {
            src[bytePos + 1] = (byte) (~(0 << bitPos) & src[bytePos + 1]);
        }
    }

   

    public static boolean getHighBitTag(byte curByte) {
        return (curByte & 0xff) > 127;
    }

    public static long[] longArrayConventor(ArrayList src) {
        int length = src.size();
        long[] result = new long[length];
        for (int i = 0; i < length; i++) {
            result[i] = src.get(i);
        }
        return result;
    }

    public static void printLongList(long[] src, int pos) {
        for (int i = pos; i < src.length; i++) {
            System.out.println(i + ":" + src[i]);
        }
    }

    public static void setIntersectBitForDecompressData(byte[] srcA,
            long bitIndex, int bitValue) {
        int srcLength = srcA.length;
        int bytePos = (int) (bitIndex / 8);
        if (bytePos + 1 > srcLength) {
            return;
        }
        int bitPos = (int) (bitIndex % 8);
        if (bitValue == 0) {
            srcA[bytePos] = (byte) (~(0x1 << bitPos) & srcA[bytePos]);
        } else {
            srcA[bytePos] = (byte) (~(0x0 << bitPos) & srcA[bytePos]);
        }
    }
   
    public static void setUnoinBitForDecompressData(byte[] srcA,
            long bitIndex, int bitValue) {
        int srcLength = srcA.length;
        int bytePos = (int) (bitIndex / 8);
        if (bytePos + 1 > srcLength) {
            return;
        }
        int bitPos = (int) (bitIndex % 8);
        if (bitValue == 0) {
            srcA[bytePos] = (byte) ((0x0 << bitPos) | srcA[bytePos]);
        } else {
            srcA[bytePos] = (byte) ((0x1 << bitPos) | srcA[bytePos]);
        }
    }

}
public class ConstDef {
    public final static byte ZERO_BYTE_TAG = (byte) 192; // 1100 0000
    public final static byte ONE_BYTE_TAG = (byte) 193; // 1100 0001
    public final static byte BYTE_TAG_LENGTH = 1;

    public final static byte ZERO_DOUBLE_BYTE_TAG = (byte) 160; // 1010 0000
    public final static byte ONE_DOUBLE_BYTE_TAG = (byte) 161; // 1010 0001
    public final static byte DOUBLE_BYTE_TAG_LENGTH = 2;

    public final static byte ZERO_INT_TAG = (byte) 144; // 1001 0000
    public final static byte ONE_INT_TAG = (byte) 145; // 1001 0001
    public final static byte INT_TAG_LENGTH = 4;

    public final static byte ZERO_LONG_TAG = (byte) 134; // 1000 1000
    public final static byte ONE_LONG_TAG = (byte) 135; // 1000 1001
    public final static byte LONG_TAG_LENGTH = 8;

    public final static int COMPRESS_THRESHOLD = 14;
    public final static int HEADER_LENGTH = 8;
   
    public final static boolean IS_PRINTLOG = false;
    public final static int repeateBits = 3;
    public final static long unitBits = 7;
   
    public final static int repeateMax = 16777215;
    public final static int maxbufferSize = 13107200;//Integer.MAX_VALUE / 2;
}