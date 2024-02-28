package cc.catman.coder.workbench.core.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpHelper {

    public static String CIDR_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/(\\d{1,2})$";

    public static  String IP_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";


    public static boolean isCIDR(String input) {
        // CIDR表示法的正则表达式
        Pattern pattern = Pattern.compile(CIDR_REGEX);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static boolean isIPAddress(String input) {
        // IPv4地址的正则表达式
        Pattern pattern = Pattern.compile(IP_REGEX);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static boolean isIPInSubnet(String ipAddress, String subnetCIDR) {
        try {
            InetAddress address = InetAddress.getByName(ipAddress);

            // 获取IP地址和子网掩码的字节数组
            byte[] ipAddressBytes = address.getAddress();
            String[] subnetParts = subnetCIDR.split("/");
            InetAddress subnetAddress = InetAddress.getByName(subnetParts[0]);
            byte[] subnetBytes = subnetAddress.getAddress();

            // 获取子网掩码的长度
            int subnetLength = Integer.parseInt(subnetParts[1]);

            // 计算IP地址和子网掩码的前缀
            byte[] ipAddressPrefix = getPrefix(ipAddressBytes, subnetLength);
            byte[] subnetPrefix = getPrefix(subnetBytes, subnetLength);

            // 比较前缀是否相同
            for (int i = 0; i < ipAddressPrefix.length; i++) {
                if (ipAddressPrefix[i] != subnetPrefix[i]) {
                    return false;
                }
            }

            return true;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static byte[] getPrefix(byte[] bytes, int length) {
        int prefixLength = length / 8;
        byte[] prefix = new byte[prefixLength];

        for (int i = 0; i < prefixLength; i++) {
            prefix[i] = bytes[i];
        }

        int remainingBits = length % 8;
        if (remainingBits > 0) {
            int mask = 0xFF << (8 - remainingBits);
            prefix[prefixLength - 1] = (byte) (bytes[prefixLength - 1] & mask);
        }

        return prefix;
    }

}
