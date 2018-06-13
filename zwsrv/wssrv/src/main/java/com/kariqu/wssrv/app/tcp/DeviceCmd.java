package com.kariqu.wssrv.app.tcp;

/**
 * Created by simon on 10/05/2018.
 */
public interface DeviceCmd {

    static enum CmdId {
        QUERY_DEVICE_ID(0x01, "QUERY_DEVICE_ID"),
        DROP_COINS(0x02, "DROP_COINS"),
        DROP_COINS_CONFIRM(0x03, "DROP_COINS_CONFIRM"),
        QUERY_REWARD_COINS(0x06, "QUERY_REWARD_COINS");

        @Override
        public String toString() {
            return str;
        }

        public static CmdId valueOf(int val)
        {
            switch (val)
            {
                case 0x01: return QUERY_DEVICE_ID;
                case 0x02: return DROP_COINS;
                case 0x03: return DROP_COINS_CONFIRM;
                case 0x06: return QUERY_REWARD_COINS;
                default:
                    return null;
            }
        }

        private int cmdId;
        private String str;
        private CmdId(int cmdId, String str) {
            this.cmdId = cmdId;
            this.str = str;
        }

    };

    //投币和计币分开
    //投币: 这个“投币指令”，不用等待返回才能投下一次; 按照返回的次数来算投币的个数；
    //计币: 计币要一直发，2s-3s一次

    //1. 获取下位机ID（指令ID：01）
    //8A0301010555 -> 8A 0A 01 02 F4 06 01 55 70 62 05 34 55
    static final public byte[] CMD_QUERY_DEVICE_ID = {(byte)0x8A,(byte)0x03,(byte)0x01,(byte)0x01,(byte)0x05,(byte)0x55};

    //2. 投币命令（指令ID：02）
    //8A 04 02 01 01 08 55 -> 8A 04 02 02 01 09 55
    static final public byte[] CMD_DROP_COINS = {(byte)0x8A,(byte)0x04,(byte)0x02,(byte)0x01,(byte)0x01,(byte)0x08,(byte)0x55};



    //3. 输出信号命令（指令ID：03）
    //8A 04 03 01 01 09 55 -> 8A 03 03 02 08 55
    static final public byte[] CMD_DROP_COINS_CONFIRM = {(byte)0x8A, (byte)0x04, (byte)0x03, (byte)0x01, (byte)0x01, (byte)0x09, (byte)0x55};

    // 雨刷：8A 04 03 01 02 0A 55
    static final public byte[] CMD_WIPER = { (byte)0x8A, (byte)0x04, (byte)0x03, (byte)0x01, (byte)0x02, (byte)0x0A, (byte)0x55 };



    //4. 复位下位机（指令ID：04）

    //5. 上位机查询礼物（指令ID：05）

    //6. 上位机主动查询计币信号（指令ID：06）
    //8A 03 06 01 0A 55 -> 8A 04 06 02 礼品计数01 0D 55
    static final public byte[] CMD_QUERY_REWARD_COINS = {(byte)0x8A,(byte)0x03,(byte)0x06,(byte)0x01,(byte)0x0A,(byte)0x55};


}
