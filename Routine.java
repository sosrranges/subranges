import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

public class Routine {

    public static ArrayList<RangeRule> getBiggestOrderIndependentGroup(ArrayList<RangeRule> rules, boolean[] used) {
        ArrayList<RangeRule> subrules = new ArrayList<>();
        for (RangeRule r : rules) {
            subrules.add(r.getSubRule(used));
        }
        HashSet<Integer> ansId = new HashSet<>();
        for (int i = 0; i < rules.size(); i++) {
            boolean bad = false;
            for (Integer rID : ansId) {
                if (!subrules.get(i).isMatchDisjoint(subrules.get(rID))) {
                    bad = true;
                    break;
                }
            }
            if (!bad) {
                ansId.add(i);
            }
        }
        ArrayList<RangeRule> ans = new ArrayList<>();
        for (int i = 0; i < rules.size(); i++) {
            if (ansId.contains(i)) {
                ans.add(rules.get(i));
            }
        }
        return ans;
    }

    public static long calculateRules(ArrayList<RangeRule> rules, boolean[] used, int mode) {
        BigInteger ans = BigInteger.ZERO;
        for (RangeRule r : rules) {
            ans=ans.add(r.getSubRule(used).getNumberOfRules(mode));
        }
        return ans.longValue();
    }

    public static BigInteger[] calculateRulesWithoutBits(ArrayList<RangeRule> rules, boolean[] used, int mode) {
        BigInteger[] values = new BigInteger[used.length];
        for (int i = 0; i < used.length; i++) {
            if (used[i]) {
                values[i] = BigInteger.ZERO;
            }
        }
        for (RangeRule r : rules) {
            RangeRule subrule = r.getSubRule(used);
            BigInteger orig = subrule.getNumberOfRules(mode);
            for (int i = 0; i < Generator.num_ranges; i++) {
                SubRange curRange = r.ranges.get(i);
                SubRange curSubRange = subrule.ranges.get(i);

                BigInteger orig1 = orig.divide(BigInteger.valueOf(curSubRange.getRules(mode)));
                boolean[] arr = curSubRange.arr.clone();
                for (int j = 0; j < Generator.range_size; j++) {
                    if (arr[j]) {
                        arr[j] = false;
                        BigInteger temp = BigInteger.valueOf(new SubRange(curRange.orL, curRange.orR, Generator.range_size, arr).getRules(mode));
                        values[j+i*Generator.range_size] = values[j+i*Generator.range_size].add(orig1.multiply(temp));
                        arr[j] = true;
                    }
                }
            }
        }
        return values;
    }

    public static ArrayList<RangeRule> applyForwardSubsumptionOnOriginal(ArrayList<RangeRule> rules) {
        ArrayList<RangeRule> toRemove = new ArrayList<>();
        for (int i = 0; i < rules.size(); i++) {
            for (int j = i+1; j < rules.size(); j++) {
                if (rules.get(i).isCover(rules.get(j))) {
                    toRemove.add(rules.get(j));
                }
            }
        }

        rules.removeAll(toRemove);
        return rules;
    }


}

