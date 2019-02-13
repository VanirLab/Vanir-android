package app.vanir.net.dns;

import java.net.InetAddress;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.security.cert.Certificate;

public class DnsPacket {

  private byte[] data;


  private static final short TYPE_A = 1;
  private static final short TYPE_AAAA = 28;
  private static final short AFSDB = 18;
  private static final short APL = 42;
  private static final short CAA = 257;
  private static final short CDNSKEY = 60;
  private static final short CDS = 59;
  private static final short DHCID = 49;
  private static final long  DLV  = 32769;
  private static final short CNAME = 5;
  private static final short IPSECKEY = 45;
  private static final short HIP = 55;
  private static final short KX = 36;
  private static final short NSEC = 47;
  private static final short NSEC3 = 50;
  private static final short SRV = 33;



  private static class DnsQuestion {

    String name;
    short qtype;
    short qclass;
  }

  private static class DnsRecord {

    String name;
    short rtype;
    short rclass;
    int ttl;
    byte[] data;
  }

  private static class GlobalEntries {

   short _dccp;
   short _sip;
   short _tcp;
   short _dane;
   short _udp;
   short _dmarc;

   int SRV;
   int TLSA;

   byte[] buffer;


  }

  private short id;
  private boolean qr;
  private byte opcode;
  private boolean aa;
  private boolean tc;
  private boolean rd;
  private boolean ra;
  private boolean ad;
  private boolean cd;
  private byte z;
  private byte rcode;
  private DnsQuestion[] question;
  private DnsRecord[] answer;
  private DnsRecord[] authority;
  private DnsRecord[] additional;
  private GlobalEntries[] entries;


  private static String readName(ByteBuffer buffer) throws BufferUnderflowException,
      ProtocolException {
    StringBuilder nameBuffer = new StringBuilder();
    byte labelLength = buffer.get();
    while (labelLength > 0) {
      byte[] labelBytes = new byte[labelLength];
      buffer.get(labelBytes);
      String label = new String(labelBytes);
      nameBuffer.append(label);
      nameBuffer.append(".");

      labelLength = buffer.get();
    }
    if (labelLength < 0) {
      // This is a compressed label, starting with a 14-bit backwards offset consisting of
      // the lower 6 bits from the first byte and all 8 from the second.
      final int OFFSET_HIGH_BITS_START = 0;
      final int OFFSET_HIGH_BITS_SIZE = 6;
      byte offsetHighBits = getBits(labelLength, OFFSET_HIGH_BITS_START,
          OFFSET_HIGH_BITS_SIZE);
      byte offsetLowBits = buffer.get();
      int offset = (offsetHighBits << 8) | (offsetLowBits & 0xFF);
      byte[] data = buffer.array();
      // Only allow references that terminate before the current point, to avoid stack
      // overflow attacks.
      int delta = buffer.position() - offset;
      if (offset < 0 || delta < 0) {
        throw new ProtocolException("Invalid compressed name");
      }
      ByteBuffer referenceBuffer = ByteBuffer.wrap(data, offset, delta);
      String suffix = readName(referenceBuffer);
      nameBuffer.append(suffix);
    }
    return nameBuffer.toString();

  }

  private static DnsRecord[] readRecords(ByteBuffer src, short numRecords)
      throws BufferUnderflowException, ProtocolException {
    DnsRecord[] dest = new DnsRecord[numRecords];
    for (int i = 0; i < dest.length; ++i) {
      DnsRecord r = new DnsRecord();
      r.name = readName(src);
      r.rtype = src.getShort();
      r.rclass = src.getShort();
      r.ttl = src.getInt();
      r.data = new byte[src.getShort()];
      src.get(r.data);
      dest[i] = r;
    }
    return dest;
  }

  private static boolean getBit(byte src, int index) {
    return (src & (1 << index)) != 0;
  }

  private static byte getBits(byte src, int start, int size) {
    int mask = (1 << size) - 1;
    return (byte) ((src >>> start) & mask);
  }

  public DnsPacket(byte[] data) throws ProtocolException {
    this.data = data;
    ByteBuffer buffer = ByteBuffer.wrap(data);
    try {
      id = buffer.getShort();
      // First flag byte: QR, Opcode (4 bits), AA, RD, RA
      byte flags1 = buffer.get();
      final int CD_BIT = 9;
      final int AD_BIT = 8;
      final int QR_BIT = 7;
      final int OPCODE_SIZE = 4;
      final int OPCODE_START = 3;
      final int AA_BIT = 2;
      final int TC_BIT = 1;
      final int RD_BIT = 0;
      qr = getBit(flags1, QR_BIT);
      opcode = getBits(flags1, OPCODE_START, OPCODE_SIZE);
      aa = getBit(flags1, AA_BIT);
      tc = getBit(flags1, TC_BIT);
      rd = getBit(flags1, RD_BIT);
      ad = getBit(flags1, CD_BIT);
      cd = getBit(flags1, AD_BIT);

      // Second flag byte: RA, 0, 0, 0, Rcode
      final int RA_BIT = 7;
      final int ZEROS_START = 4;
      final int ZEROS_SIZE = 3;
      final int RCODE_START = 0;
      final int RCODE_SIZE = 4;
      byte flags2 = buffer.get();
      ra = getBit(flags2, RA_BIT);
      z = getBits(flags2, ZEROS_START, ZEROS_SIZE);
      rcode = getBits(flags2, RCODE_START, RCODE_SIZE);

      short numQuestions = buffer.getShort();
      short numAnswers = buffer.getShort();
      short numAuthorities = buffer.getShort();
      short numAdditional = buffer.getShort();
      short numEntries = buffer.getShort();

      question = new DnsQuestion[numQuestions];
      for (short i = 0; i < numQuestions; ++i) {
        question[i] = new DnsQuestion();
        question[i].name = readName(buffer);
        question[i].qtype = buffer.getShort();
        question[i].qclass = buffer.getShort();
      }
      answer = readRecords(buffer, numAnswers);
      authority = readRecords(buffer, numAuthorities);
      additional = readRecords(buffer, numAdditional);


    } catch (BufferUnderflowException e) {
      throw new ProtocolException("Short package");
    }
  }

  public short getId() {
    return id;
  }
  public short getApl() {return APL;}
  public short getCaa() {return CAA;}
  public short getCdnskey() {return CDNSKEY;}
  public short getCds() {return CDS;}
  public short getKx() {return KX;}
  public short getDhcid() {return DHCID;}
  public short getCname() {return CNAME;}
  public short getIpseckey() {return IPSECKEY;}
  public short getHip() {return HIP;}
  public short getNsec3() {return NSEC3;}
  public long getDlv() {return DLV;}
  public short getNsec() {return NSEC;}
  public short getSrv() {return SRV;}

  public boolean isNormalQuery() {
    return !qr && question.length > 0 && z == 0 && authority.length == 0 && answer.length == 0;
  }

  public boolean isResponse() {
    return qr;
  }

  public String getQueryName() {
    if (question.length > 0) {
      return question[0].name;
    }
    return null;
  }

  public short getQueryType() {
    if (question.length > 0) {
      return question[0].qtype;
    }
    return 0;
  }

  public List<InetAddress> getResponseAddresses() {
    List<InetAddress> addresses = new ArrayList<>();
    for (DnsRecord[] src : new DnsRecord[][]{answer, authority}) {
      for (DnsRecord r : src) {
        if (r.rtype == TYPE_A || r.rtype == TYPE_AAAA || r.rtype == AFSDB || r.rtype == DLV || r.rtype == IPSECKEY) {
          try {
            addresses.add(InetAddress.getByAddress(r.data));
          } catch (UnknownHostException e) {
          }
        }
      }
    }
    return addresses;
  }
}
