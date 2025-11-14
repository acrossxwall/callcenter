package cc.efit.dial.biz.constants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;

public class DialConstants {

    public static final Map<String,String> CALL_UUID_CALL_ID_MAP = new ConcurrentHashMap<>();



    public static final ConcurrentMap<String, Long> lastSeqMap = new ConcurrentHashMap<>();
    public static final ConcurrentMap<String, ReadWriteLock> lockMap = new ConcurrentHashMap<>();
}
