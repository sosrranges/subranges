import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class RangeRule {
     ArrayList<SubRange> ranges = new ArrayList<>();

     public RangeRule(ArrayList<SubRange> ranges) {
         this.ranges = new ArrayList<>(ranges);
     }

     public BigInteger getNumberOfRules(int mode) {
         BigInteger ans = BigInteger.ONE;
         for (SubRange range : ranges) {
             ans = ans.multiply(BigInteger.valueOf(range.getRules(mode)));
         }
         return ans;
     }

    public RangeRule getSubRule(boolean[] used) {
         int pos = 0;
         ArrayList<SubRange> ans = new ArrayList<>();
         for (SubRange rr : ranges) {
            boolean[] cur = new boolean[rr.w];
            for (int i = 0; i < rr.w; i++) {
                cur[i] = used[pos+i];
            }
            ans.add(new SubRange(rr.orL, rr.orR, rr.w, cur));
            pos += rr.w;
         }
         if (pos != used.length) {
             throw new AssertionError();
         }
         return new RangeRule(ans);

     }

     public boolean doesIntersect(RangeRule another) {
            for (int i = 0; i < ranges.size(); i++) {
                if (!ranges.get(i).isIntersect(another.ranges.get(i))) {
                    return false;
                }
            }
            return true;
     }

    public boolean isMatchDisjoint(RangeRule another) {
        if (!doesIntersect(another)) {
            return true;
        }
        //if (true) {
        //    return false;
        //}

        for (int i = 0; i < ranges.size(); i++) {
            if (!ranges.get(i).isMatchDisjoint(another.ranges.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isCover(RangeRule another) {
        for (int i = 0; i < ranges.size(); i++) {
            if (!ranges.get(i).isCoverOriginally(another.ranges.get(i))) {
                return false;
            }
        }
        return true;
    }
}
