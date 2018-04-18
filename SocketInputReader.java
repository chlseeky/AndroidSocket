import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * SocketInputReader
 * Feature:
 */
public class SocketInputReader extends Reader {
    final SocketInputReader self = this;

    public InputStream inputStream;


    /* Constructors */
    public SocketInputReader(InputStream inputStream) {
        super(inputStream);
        this.inputStream = inputStream;
    }

    /* Public Methods */
    
    
    /* Properties */

    /* Overrides */
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            if (this.inputStream != null) {
                this.inputStream.close();
                this.inputStream = null;
            }
        }
    }

    @Override
    public int read(char[] buffer, int offset, int count) throws IOException {
        throw new IOException("read() is not support for SocketInputReader, try readBytes().");
    }

    /**固定读取 8192 的长度，提高读取的效率 2700 80*/
    public byte[] readBytes() throws IOException {
        synchronized (lock) {
            if (!internalIsOpen()) {
                throw new IOException("InputStreamReader is closed");
            }
            try {
//                long time1 = System.currentTimeMillis();
                byte[] bytes = new byte[8192];//设置一次读取的最大包长度  8192
                int length = inputStream.read(bytes);//这里会阻塞while循环
                if(length <= 0){
                    return null;
                }
                byte[] result = new byte[length];
                System.arraycopy(bytes,0,result,0,length);
//                long times = System.currentTimeMillis() - time1;
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**读取连在一起的流*/
    public byte[] readBytes(byte[] header, byte[] trailer) throws IOException {
        synchronized (lock) {
            if (!internalIsOpen()) {
                throw new IOException("InputStreamReader is closed");
            }
            try {
                ArrayList<Byte> list = new ArrayList<>();
                int c;
                boolean readOver = false;
//                int headerCount = header == null ? 0 : header.length;
//                int trailerCount = trailer == null ? 0 : trailer.length;
//                boolean isHeaderMatched = false;
//                int matchHeaderIndex = 0;
//                int matchTrailerIndex = 0;

//                byte[] tryRemovedTrailer = new byte[trailerCount];
//                long old0 = System.currentTimeMillis();
                while (-1 != (c = this.inputStream.read())) {
//                    if (header != null) {
//                        if (matchHeaderIndex < headerCount) {
//                            if (header[matchHeaderIndex] == c) {
//                                matchHeaderIndex++;
//
//                                if (matchHeaderIndex == headerCount) {
//                                    if (isHeaderMatched && Arrays.equals(header, trailer)) {
//                                        readOver = true;
//                                        break;
//                                    }
//                                    isHeaderMatched = true;
//                                    list.clear();
//                                    matchHeaderIndex = 0;
//                                    continue;
//                                } else {
//                                    if (!isHeaderMatched) {
//                                        continue;
//                                    }
//                                }
//                            } else {
//                                matchHeaderIndex = 0;
//
//                                if (!isHeaderMatched) {
//                                    continue;
//                                }
//                            }
//                        }
//                    }
//                    if (trailer != null) {
//                        if (matchTrailerIndex < trailerCount) {
//                            if (trailer[matchTrailerIndex] == c) {
//                                tryRemovedTrailer[matchTrailerIndex] = (byte) c;
//                                matchTrailerIndex++;
//
//                                if (matchTrailerIndex == trailerCount) {
//                                    readOver = true;
//                                    break;
//                                }
//                            } else {
//                                for (int i = 0; i < matchTrailerIndex; i++) {
//                                    list.add(tryRemovedTrailer[i]);
//                                }
//                                matchTrailerIndex = 0;
//                                list.add((byte) c);
//                            }
//                        }
//                    } else {
                        list.add((byte) c);
                        if (this.inputStream.available() == 0) {//到达流的末尾
                            readOver = true;
                            break;
                        }
//                    }
                }
//                long all0 = System.currentTimeMillis() - old0;
//                Log.e("这个while循环过程耗时",""+all0 +"  "+ list.size());

//                long old = System.currentTimeMillis();
//                byte[] bb  = new byte[inputStream.available()];//这里的inputStream.available()就是获取这个inputstream的大小
//                inputStream.read(bb);
//                long all = System.currentTimeMillis() - old;
//                Log.e("这个read过程耗时",""+all +"  "+ bb.length);

                if (!readOver) {
                    return null;
                }

                if (list.size() == 0) {
                    return null;
                }
                byte[] result = new byte[list.size()];
                Iterator<Byte> iterator = list.iterator();
                for (int i = 0; i < result.length; i++) {
                    result[i] = iterator.next();
                }
                return result;
            } catch (IOException e) {
                return null;
            }
        }
    }

    /**读取指定长度的流*/
    public byte[] readBytesS(byte[] header, byte[] trailer) throws IOException {
        synchronized (lock) {
            if (!internalIsOpen()) {
                throw new IOException("InputStreamReader is closed");
            }

            try {
                byte[] ccpBuffer = new byte[8192];
                int nHaveRead = 0;
                //首先从流中读取16个字节，流中的内容少了16字节
                while (nHaveRead < 16) {
                    nHaveRead = this.inputStream.read(ccpBuffer, 0, 16 - nHaveRead);
                }

                //再根据前16字节的数据读出制定大小的流，流中的内容少了nLen
                int nLen = ccpBuffer[0];
                if (nLen < 0) nLen += 256;
                if (ccpBuffer[1] < 0)
                    nLen += (ccpBuffer[1] + 256) * 256;
                else
                    nLen += (ccpBuffer[1]) * 256;
                nHaveRead = 0;
                while (nHaveRead < nLen) {
                    nHaveRead = this.inputStream.read(ccpBuffer, 0, nLen - nHaveRead);
                }

                byte[] result = new byte[nLen];
                System.arraycopy(ccpBuffer, 0, result, 0, nLen);

                return result;
            }catch (IOException e){
                return null;
            }
        }
    }

    @Override
    public boolean ready() throws IOException {
        synchronized (lock) {
            if (this.inputStream == null) {
                throw new IOException("InputStreamReader is closed");
            }
            try {
                return this.inputStream.available() > 0;
            } catch (IOException e) {
                return false;
            }
        }
    }

    /* Delegates */


    /* Private Methods */
    public static void internalCheckOffsetAndCount(int arrayLength, int offset, int count) {
        if ((offset | count) < 0 || offset > arrayLength || arrayLength - offset < count) {
            throw new ArrayIndexOutOfBoundsException("arrayLength=" + arrayLength + "; offset=" + offset
                    + "; count=" + count);
        }
    }

    private boolean internalIsOpen() {
        return this.inputStream != null;
    }
}