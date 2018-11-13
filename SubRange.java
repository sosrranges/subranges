import java.lang.reflect.Array;
import java.util.Arrays;

public class SubRange {
    public long orL, orR;
    long[] ls, rs;
    int w;
    boolean[] arr;

    boolean isEmpty;

    public SubRange(long l, long r, int w) {
        orL = l;
        orR = r;
        ls = new long[]{1, 1};
        rs = new long[]{0, 0};
        ls[0] = orL;
        rs[0] = orR;
        arr = new boolean[w];
        Arrays.fill(arr, true);
        this.w = w;
        isEmpty = false;
    }


    public SubRange(long l, long r, int w, boolean[] take) {
        orL = l;
        orR = r;
        ls = new long[]{1, 1};
        rs = new long[]{0, 0};
        arr = take.clone();
        for (boolean i : take) {
            if (i) this.w++;
        }

        if (this.w == 0) {
            isEmpty = true;
            return;
        }

        isEmpty = false;
        if (l == r) {
            ls[0] = compress(l, w, take);
            rs[0] = compress(r, w, take);
            return;
        }

        long sim = calculateSim(l, r);
        long[] ansL = calcRight(l, l | ((1L << (sim-1))-1), w, take);
        long[] ansR = calcLeft(r - r %(1L<<(sim-1)), r , w, take);
        constructFromIntervals(ansL[0], ansL[1], ansR[0], ansR[1]);
    }


    private static long compress(long x, int w, boolean[] takes)
    {
        long val = 0;
        for (int i = 0; i < takes.length; i++) {
            if (takes[i]) {
                val = val*2;
                if ((x & (1L << (w - i - 1))) != 0) {
                    val = val + 1;
                }
            }
        }
        return val;
    }

    private int calculateSim(long l, long r) {
        int ans = 0;
        while (l!=r) {
            ans++;
            l /= 2;
            r /= 2;
        }
        return ans;
    }

    private long[] calcLeft(long l, long r, int w, boolean[] b) {
        boolean meetDifference = false;
        for (int i = 1; i <= w; i++) {
            long p = (1L << (w - i));
            meetDifference |= ((l &p) != (r&p));
            if (meetDifference & ((r&p) != 0) & !b[i-1]) {
                r = (r | (p-1))-p;
                break;
            }
        }
        return new long[]{compress(l, w, b), compress(r, w, b)};
    }

    private long[] calcRight(long l, long r, int w, boolean[] b) {
        boolean meetDifference = false;
        for (int i = 1; i <= w; i++) {
            long p = (1L << (w - i));
            meetDifference |= ((l &p) != (r&p));
            if (meetDifference & ((l&p) == 0) & !b[i-1]) {
                l = l + p - l%p;
            }
        }
        return new long[]{compress(l, w, b), compress(r, w, b)};
    }

    private boolean isIntersect(long l, long r, long l1, long r1) {
        return Math.max(l,l1)  <= Math.min(r, r1);
    }


    boolean isIntersect(SubRange o) {
        if (isEmpty) {
            return true;
        }
        for (int i = 0; i < 4; i++) {
            if (isIntersect(ls[i%2], rs[i%2], o.ls[i/2], o.rs[i/2])) {
                return true;
            }
        }
        return false;
    }

    boolean isMatchDisjoint(SubRange o) {
        if (!isIntersect(o)) {
            return true;
        }
        if (true) {
            return false;
        }
        if (isEmpty) {
            return orL >= o.orL && orR <= o.orR;
        }

        if (orL < o.orL && !validate(orL, Math.min(orR, o.orL-1), o)) {
            return false;
        }
        if (orR > o.orR && !validate(Math.max(orL, o.orR+1),orR, o)) {
            return false;
        }
        return true;
    }

    private boolean validate(long l, long r, SubRange o) {
        SubRange test = new SubRange(l, r, arr.length, arr);
        return !test.isIntersect(o);
    }


    private void constructFromIntervals(long l, long r, long l1, long r1) {
        if (isIntersect(l, r+1, l1, r1+1)) {
            ls[0] = Math.min(l,l1);
            rs[0] = Math.max(r, r1);
        } else {
            ls[0] = l; ls[1] = l1;
            rs[0] = r; rs[1] = r1;
        }
    }

    boolean hasValue(long x) {
        return (x >= ls[0] && x <= rs[0]) || (x >= ls[1] && x <= rs[1]);
    }

    public long getRules(int mode) {
        if (isEmpty) return 1;
        long ans = 0;
        if (ls[0] <= rs[0]) {
            ans += Encoding.encodeCalc(w, ls[0], rs[0], mode);
        }
        if (ls[1] <= rs[1]) {
            ans += Encoding.encodeCalc(w, ls[1], rs[1], mode);
        }
        return ans;
    }

    public String toString() {
        return ls[0] +" "+rs[0] +" "+ls[1] +" "+rs[1];
    }

    public boolean isCoverOriginally(SubRange subRange) {
        return orL <= subRange.orL && orR >= subRange.orR;
    }
}
