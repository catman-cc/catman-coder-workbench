package cc.catman.coder.workbench.core.message.netty.serialize;

/**
 * 一个简单的工具类,
 * - 用于将byte数组转换为8bit的int,以及将8bit的int转换为byte数组
 * - 将16进制的数值转换为byte数组,以及将byte数组转换为16进制的数值
 * - 将字符串描述的16进制数值转换为byte数组,以及将byte数组转换为字符串描述的16进制数值
 * - 将字符串描述的10进制数值转换为byte数组,以及将byte数组转换为字符串描述的10进制数值
 */
public class SerializeDescHelper {
    public static boolean match(String src, String target){
        if (src == null || target == null) {
            return false;
        }
        return src.equalsIgnoreCase(target);
    }

    public static boolean match(byte src, byte target){
        return src == target;
    }

    public static boolean match(int src, int target){
        return src == target;
    }

    public static boolean match(byte src,String target){
        return match(src,read(target));
    }

    public static byte read(String desc){
        // 判断是否为16进制或者10进制的数值
        if(desc.startsWith("0x")){
            return hexStringToBytes(desc);
        }else{
            return intToBytes(Integer.parseInt(desc));
        }
    }


    public static byte hexStringToBytes(String hexString) {
        return (byte) Integer.parseInt(hexString, 16);
    }

    public static byte intToBytes(int value) {
        return (byte) value;
    }

}
