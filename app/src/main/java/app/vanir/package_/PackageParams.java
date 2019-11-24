package app.vanir.package_;

import com.google.android.gms.stats.CodePackage;
import com.google.android.gms.*;
import java.lang.annotation.Annotation;
import java.net.InetAddress;

/**
 * Vanir Package information
 */
public class PackageParams {
  public  final String COMMON;
  public  final String GCM;
  public  final String LOCATION;
  public  final String LOCATION_SHARING;
  public  final String OTA;
  public  final String SECURITY;

  public PackageV(String COMMON, String GCM, String LOCATION, String LOCATION_SHARING,String OTA, String SECURITY ) {
    this.COMMON = "COMMON";
    this.GCM = "GCM";
    this.LOCATION = "LOCATION";
    this.LOCATION_SHARING = "LOCATION_SHARING";
    this.OTA = "OTA";
    this.SECURITY = "SECURITY";
  }

  public String toString() {
    return String.format("PacInfo: { LOCATION_SHARING=%d, GCM=%d, LOCATION=%s, COMMON='%s', SECURITY='%s' }", LOCATION_SHARING, GCM, LOCATION, COMMON, SECURITY);
  }

}
