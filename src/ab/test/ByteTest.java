package ab.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class ByteTest {

    public static void main(String[] args) {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("", null);
        map.put(null, null);
        System.out.println(map);


//        long l = 0xff00;
//        System.out.println(Long.toBinaryString(l));
//        byte[] res = longToBytes(l);
//
//        System.out.println(Arrays.toString(res));
//
//        byte[] ar = new byte[]{0, -1, -1, -1, -1, -1, -1, -1, -1};
//
//        long otherTime = IntStream.range(1,9).mapToLong(i->res[i]&255L).reduce((a,b)-> (a<<8)|b).orElse(0);
//        System.out.println(otherTime);
//        System.out.println(Long.toBinaryString(otherTime));




//        long gg = 0xff000000000000L; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


//        System.out.println(Long.toBinaryString(Long.MIN_VALUE>>4)); //1111100000000000000000000000000000000000000000000000000000000000
//        System.out.println(Long.toBinaryString(Long.MIN_VALUE>>>4));//100000000000000000000000000000000000000000000000000000000000

//        System.out.println(Integer.MIN_VALUE&-1L);//-2147483648
//        long ll = Long.parseLong("11111111111111111111111111111111", 2);
//        System.out.println(Integer.MIN_VALUE&ll); //2147483648



//        byte b = (byte) 124;
//        int i = b;
//        byte c = (byte) i;

//        System.out.println(b);
//        System.out.println(i);
//        System.out.println(c);
//        System.out.println(b&255);
    }

    static byte[] longToBytes(long l) {
        return new byte[] {0, (byte)(l>>>56&255), (byte)(l>>>48&255), (byte)(l>>>40&255), (byte)(l>>>32&255),
                (byte)(l>>>24&255), (byte)(l>>>16&255), (byte)(l>>>8&255), (byte)(l&255)};
    }

    static long bytesToLong(byte[] ar) {
        return IntStream.range(1,9).mapToLong(i->ar[i]&255L).reduce((a,b)-> (a<<8)|b).orElse(0);
    }
}
