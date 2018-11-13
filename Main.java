import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;

public class Main {

    static PrintWriter out1;
    static PrintWriter out2;


    public static long[] solver(ArrayList<RangeRule> rules, int metric, int fields) {
        rules = new ArrayList<>(rules);
        long[] ans = new long[4];
        for (int it = 0; rules.size() > 0; it++) {
            ArrayList<RangeRule> oldRules = new ArrayList<>(rules);
            rules = new Saxpac().getBiggestGroup(rules, fields, ans, metric);
            System.err.println(it + " " + oldRules.size() + " " + rules.size());
            boolean[] used = new boolean[Generator.sizew];
            Arrays.fill(used, true);
            System.err.println(Routine.calculateRules(rules, used, 1) + " " + Routine.calculateRules(rules, used, 2) + " " + Routine.calculateRules(rules, used, 3));

            ArrayList<Integer> cands = new ArrayList<>();
            for (int i = 0; i < used.length; i++) {
                cands.add(i);
            }
            while (cands.size() > 0) {
                BigInteger[] cand = Routine.calculateRulesWithoutBits(rules, used, metric);
                cands.sort(Comparator.comparing(x -> cand[x]));
                while (cands.size() > 0) {
                    int x = cands.get(0);
                    used[x] = false;
                    cands.remove(0);
                    if (Routine.getBiggestOrderIndependentGroup(rules, used).size() == rules.size()) {
                        break;
                    }
                    used[x] = true;
                }
                System.err.print(".");
            }
            int w = 0;
            for (int i = 0; i < used.length; i++) {
                if (used[i]) {
                    w++;
                }
            }
            ans[2] += Routine.calculateRules(rules, used, metric);
            ans[3] += Routine.calculateRules(rules, used, metric)*w;

            System.err.println();
            System.err.println(Routine.calculateRules(rules, used, 1) + " " + Routine.calculateRules(rules, used, 2) + " " + Routine.calculateRules(rules, used, 3));
            for (int i = 0; i < used.length; i++) {
                if (used[i]) {
                    System.err.print("0");
                } else {
                    System.err.print("1");
                }
            }
            System.err.println();
            oldRules.removeAll(rules);
            System.err.println("=====");
            rules = new ArrayList<>(oldRules);
        }
        return ans;
    }


    private static void solve() {
        ArrayList<RangeRule> rules = Generator.generateClassifier(10000);
        for (int metric = 1; metric <= 2; metric++)
        {
            boolean[] used = new boolean[Generator.sizew];
            Arrays.fill(used, true);
            System.err.println();
            printNumbers(metric, (long)Generator.num_ranges, (long)Generator.range_size, Routine.calculateRules(rules, used, metric), Routine.calculateRules(rules, used, metric) * Generator.sizew);
            for (int fields = 1; fields <= 2; fields++) {
                long[] ans = solver(rules, metric, fields);
                int[] groups = new Saxpac().getNumberOfGroups(rules, fields);
                printNumbers(metric, (long)groups[1], (long)groups[0], ans[0], ans[1], ans[2], ans[3]);
            }
            printLineBreak(metric);
        }

    }

    static boolean isNewLine = true;
    private static void printLineBreak(int metric) {
        selectWriter(metric).println("\\\\");
        selectWriter(metric).flush();
        isNewLine = true;
    }

    private static void printNumbers(int metric, Long... numbers) {
        for (Long num : numbers) {
            printNum(selectWriter(metric), num);
        }
    }

    private static void printNum(PrintWriter printWriter, Long num) {
        String s = "";
        if (num < 1000000) {
            s += num;
        }
        else {
            int pow = 0;
            double x = num;
            while (x > 10) {
                pow++;
                x/= 10.0;
            }
            s += String.format("%.1f",Math.round(x*10)/10.0)+"\\cdot 10^{"+pow+"}";
        }
        if (!isNewLine) {
            printWriter.print(" & ");
        }
        isNewLine = false;
        printWriter.print("$"+s+"$");
        printWriter.flush();
    }

    private static PrintWriter selectWriter(int metric) {
        return metric == 1 ? out1 : out2;
    }

    public static void main(String[] args) throws FileNotFoundException {
        out1 = new PrintWriter("output_prefix.tex");
        out2 = new PrintWriter("output_SRGE.tex");

        for (int i = 16; i <= 32; i+=4) {
            Generator.num_ranges = 4;
            Generator.range_size = i;
            Generator.sizew = Generator.range_size*Generator.num_ranges;
            solve();
        }

        out1.println("\\hline");
        out2.println("\\hline");

        for (int i = 3; i <= 6; i++) {
            Generator.num_ranges = i;
            Generator.range_size = 32;
            Generator.sizew = Generator.range_size*Generator.num_ranges;
            solve();
        }

        System.err.println("===");
        out1.println("\\hline");
        out2.println("\\hline");
        out1.close();
        out2.close();
    }

}



