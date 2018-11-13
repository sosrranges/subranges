import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Saxpac {

    public ArrayList<RangeRule> getBiggestGroup(ArrayList<RangeRule> rules, int numFields, long[] arr, int mode) {
        if (numFields == 1) {
            return getBiggestGroup1(rules, arr, mode);
        }
        else {
            return getBiggestGroup2(rules, arr, mode);
        }
    }

    public int[] getNumberOfGroups(ArrayList<RangeRule> rules, int numFields) {
        rules = new ArrayList<>(rules);
        int[] ans = new int[2];
        int orig = rules.size();
        while (rules.size() > 0) {
            ans[0]++;
            if (rules.size() > 0.05*orig) {
                ans[1]++;
            }
            long[] fict = new long[4];
            rules.removeAll(getBiggestGroup(rules, numFields, fict, 1));
        }
        return ans;
    }


    private ArrayList<RangeRule> getBiggestGroup1(ArrayList<RangeRule> rules, long[] arr, int mode) {
        ArrayList<RangeRule> best = new ArrayList<>();
        int bestField = -1;
        for (int i = 0; i < Generator.num_ranges; i++) {
            boolean[] used = setFields(i);
            ArrayList<RangeRule> cand = Routine.getBiggestOrderIndependentGroup(rules, used);
            if (cand.size() > best.size()) {
                best = cand;
                bestField = i;
            }
        }
        arr[0] += Routine.calculateRules(best, setFields(bestField), mode);
        arr[1] += Routine.calculateRules(best, setFields(bestField), mode)*Generator.range_size;

        System.err.println(bestField);
        System.err.println(best.size()+" "+Routine.calculateRules(best, setFields(bestField),1) +" "+Routine.calculateRules(best, setFields(bestField),2)+" "+Routine.calculateRules(best, setFields(bestField),3));
        return best;
    }

    private ArrayList<RangeRule> getBiggestGroup2(ArrayList<RangeRule> rules, long[] arr, int mode) {
        ArrayList<RangeRule> best = new ArrayList<>();
        int bestField1 = -1, bestField2 = -1;
        for (int i = 0; i < Generator.num_ranges; i++) {
            for (int j = i + 1; j < Generator.num_ranges; j++) {
                boolean[] used = setFields(i, j);
                ArrayList<RangeRule> cand = Routine.getBiggestOrderIndependentGroup(rules, used);
                if (cand.size() > best.size()) {
                    best = cand;
                    bestField1 = i;
                    bestField2 = j;
                }
            }
        }
        arr[0] += Routine.calculateRules(best, setFields(bestField1, bestField2), mode);
        arr[1] += Routine.calculateRules(best, setFields(bestField1, bestField2), mode)*Generator.range_size*2;
        System.err.println(bestField1+" "+bestField2);
        System.err.println(best.size()+" "+Routine.calculateRules(best, setFields(bestField1, bestField2),1) +" "+Routine.calculateRules(best, setFields(bestField1, bestField2),2)+" "+Routine.calculateRules(best, setFields(bestField1, bestField2),3));
        return best;
    }

    public boolean[] setFields(int... fields) {
        boolean[] ans = new boolean[Generator.sizew];
        for (int field : fields) {
            for (int i = 0; i < Generator.range_size; i++) {
                ans[i + Generator.range_size*field] = true;
            }
        }
        return ans;
    }
}

