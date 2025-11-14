package cc.efit.call.api.constants;

import java.util.HashMap;
import java.util.Map;

public class CallTaskPriority {

    public static final Map<Integer,Integer> PRIORITY_WEIGHTS = new HashMap<>();

    static {
        for (int i=0;i<10;i++){
            PRIORITY_WEIGHTS.put(i+1, 10-i);
        }
    }
}
