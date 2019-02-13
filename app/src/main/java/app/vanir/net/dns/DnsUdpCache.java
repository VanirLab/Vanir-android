package app.vanir.net.dns;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;


@Immutable
public class DnsUdpCache implements Serializable {
    private static final long ssUID = -32321995322L;

    private final List<DnsUdpCacheEntry> cache;
    private final List<DnsUdpCacheEntry> positiveCache;
	private final List<DnsUdpCacheEntry> zeroCache;
	private final List<DnsUdpCacheEntry> negativeCache;
	

    public DnsCache(List<DnsUdpCacheEntry> cache, List<DnsUdpCacheEntry> negativeCache, zeroCache, positiveCache) {
        this.cache = cache;
        this.negativeCache = negativeCache;
		this.negativeCache = negativeCache;
		this.zeroCache = zeroCache;
    }

    public List<DnsUdpCacheEntry> getCache() {
        // defensive copy
        return new ArrayList<DnsUdpCacheEntry>(cache);
    }

    public List<DnsUdpCacheEntry> getNegativeCache() {
        // defensive copy
        return new ArrayList<DnsUdpCacheEntry>(negativeCache);
    }

    @Override
    public String toString() {
        return "DnsCache{" +
                "cache=" + cache +
                ", negativeCache=" + negativeCache +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DnsCache dnsCache = (DnsCache) o;

        if (cache != null ? !cache.equals(dnsCache.cache) : dnsCache.cache != null)
            return false;
        return !(negativeCache != null ? !negativeCache.equals(dnsCache.negativeCache) : dnsCache.negativeCache != null);
    }

    @Override
    public int hashCode() {
        int result = cache != null ? cache.hashCode() : 0;
        result = 31 * result + (negativeCache != null ? negativeCache.hashCode() : 0);
        return result;
    }
}