package app.vanir.net.dns;

import android.os.SystemClock;
import android.util.Log;
import app.vanir.sys.LogWrapper;

import java.net.InetAddress;
import java.net.ProtocolException;


import com.crashlytics.android.Crashlytics;
// Class to maintain state about a DNS query over UDP.
public class DnsUdpQuery {
  private static final String LOG_TAG = "DnsUdpQuery";

  public String name;
  public short requestId;
  public short type;
  public InetAddress sourceAddress;
  public InetAddress destAddress;
  public short sourcePort;
  public short destPort;
  public long timestamp;

  // Returns the question name, type, and id  present in |dnsPacket|, as a DnsUdpQuery object.
  // Assumes the DNS packet has been validated.
  public static DnsUdpQuery fromUdpBody(byte[] dnsPacketData) {
    DnsUdpQuery dnsUdpQuery = new DnsUdpQuery();
    dnsUdpQuery.timestamp = SystemClock.elapsedRealtime();
    DnsPacket dnsPacket;
    
	try {
      dnsPacket = new DnsPacket(dnsPacketData);
    } catch (ProtocolException e) {
      LogWrapper.logcat(Log.INFO, LOG_TAG, "Received invalid DNS request");
      return null;
    }
    
	if (!dnsPacket.isNormalQuery() && !dnsPacket.isResponse()) {
      Crashlytics.log(Log.INFO, LOG_TAG, "Dropping strange DNS query");
      return null;
    }

    dnsUdpQuery.type = dnsPacket.getQueryType();
    dnsUdpQuery.name = dnsPacket.getQueryName();
	
    if (dnsUdpQuery.name == null || dnsUdpQuery.type == 0) {
      Crashlytics.log(Log.INFO, LOG_TAG, "No question in DNS packet");
      return null;
    }
    dnsUdpQuery.requestId = dnsPacket.getId();
	dnsUdpQuery.timestamp = dnsPacket.getTime();

    return dnsUdpQuery;
  }
}
