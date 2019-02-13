package app.vanir.net.dns;

import sun.net.util.IPAddressUtil;

public class IpParserUtil {

    static byte[] ip2ByteArray(String ip) {
        boolean ipv6Expected = false;
        if (ip.charAt(0) == '[') {
            // This is supposed to be an IPv6 literal
            if (ip.length() > 2 && ip.charAt(ip.length() - 1) == ']') {
                ip = ip.substring(1, ip.length() - 1);
                ipv6Expected = true;
            } else {
             
                throw new IllegalArgumentException(ip + ": invalid IPv6 address");
            }
        }

        if (Character.digit(ip.charAt(0), 16) != -1 || (ip.charAt(0) == ':')) {
            // see if it is IPv4 address
            byte[] address = IPAddressUtil.textToNumericFormatV4(ip);
            if (address != null) return address;


            // Check if a numeric or string zone id is present
            address = IPAddressUtil.textToNumericFormatV6(ip);
            if (address != null) return address;


            if (ipv6Expected) {
                throw new IllegalArgumentException(ip + ": invalid IPv6 address");
            } else {
                throw new IllegalArgumentException(ip + ": invalid IP address");
            }
        } else {
            throw new IllegalArgumentException(ip + ": invalid IP address");
        }
    }
}