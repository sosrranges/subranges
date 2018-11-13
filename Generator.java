import java.util.ArrayList;
import java.util.Random;

public class Generator {
     static Random rnd = new Random(239);

     public static ArrayList<RangeRule> generateClassifier(int n) {
         ArrayList<RangeRule> rules = new ArrayList<>();
         for (int i = 0; i < 10000; i++) {
             rules.add(Generator.generateRule());
         }
         rules = placeRulesNormally(rules);
         return rules;
     }

     public static ArrayList<RangeRule> placeRulesNormally(ArrayList<RangeRule> rules) {
         ArrayList<RangeRule> ans = new ArrayList<RangeRule>();
         while (rules.size() > 0) {
             ArrayList<RangeRule> toAdd = new ArrayList<>();
             for (int i = 0; i < rules.size(); i++) {
                 boolean isOk = true;
                 for (int j = i+1; isOk && j < rules.size(); j++) {
                     isOk &= !rules.get(i).isCover(rules.get(j));
                 }
                 if (isOk) {
                     toAdd.add(rules.get(i));
                 }
             }
             ans.addAll(toAdd);
             rules.removeAll(toAdd);
         }
         return ans;
     }

     public static SubRange generateRange(int w) {
        int x = rnd.nextInt(w);
        long f = generateNum(x);
        long u1 = generateNum(w-x-1);
        long u2 = generateNum(w-x-1);
        return new SubRange((f << (w-x))+u1, ((f*2+1) << (w-x-1))+u2, w);
     }

    private static long generateNum(int w) {
         long mod = (1L<<w);
         long x = (rnd.nextLong() % mod + 3*mod) % mod;
         return x;
    }

    static int range_size = 30;
    static int num_ranges = 3;
    static int sizew = range_size*num_ranges;
    public static RangeRule generateRule() {
        ArrayList<SubRange> ranges = new ArrayList<>();
        for (int i = 0; i < num_ranges; i++) {
            ranges.add(generateRange(range_size));
        }
        return new RangeRule(ranges);
    }
}
