package Defaults;

/**
 * Created by zurek on 27.10.2016.
 * xxx
 */

public class CRC {
    protected static int CRC16(byte[] buf, int len){
        int crc = 0xFFFF;

        for (int pos = 0; pos < len; pos++) {
            crc ^= (int)buf[pos] & 0xFF;

            for (int i = 8; i != 0; i--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                }
                else crc >>= 1;
            }
        }
        return crc;
    }
}
