import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Encoding {

    public static String encodeBinary(long a, int w)
    {
        StringBuilder s = new StringBuilder(Long.toBinaryString(a));
        while (s.length() != w)
        {
            s.insert(0, "0");
        }
        return s.toString();
    }

    public static String generatePrefix(int w)
    {
        String ans = "";
        for (int i = 0; i < w; i++)
        {
            ans += "*";
        }
        return ans;
    }

    public static ArrayList<String> appendFirstCharactes(ArrayList<String> rules, char c)
    {
        ArrayList<String> ans = new ArrayList<>();
        for (String s : rules)
        {
            ans.add(c+s);
        }
        return ans;
    }

    public static ArrayList<String> prefixEncode(int w, long l, long r) {
        return encodeInterval(w, 0, 1L<<w, l, r);
    }

    public static long prefixEncodeCalc(int w, long l, long r) {
        return calculateEncodeInterval(w, 0, 1L<<w, l, r);
    }


    public static long calculateEncodeInterval(int w, long l, long r, long l1, long r1)
    {
        if (l == l1 && r == r1+1)
        {
            return 1;
        }
        long mid = (l+r)/2;
        long ans = 0;
        if (l1 < mid)
        {
            ans += calculateEncodeInterval(w-1, l, mid, l1, Math.min(mid-1, r1));
        }
        if (r1 >= mid)
        {
            ans += calculateEncodeInterval(w-1, mid, r, Math.max(l1, mid), r1);
        }
        return ans;
    }


    public static ArrayList<String> encodeInterval(int w, long l, long r, long l1, long r1)
    {
        if (l == l1 && r == r1+1)
        {
            return new ArrayList<>(Arrays.asList(generatePrefix(w)));
        }
        long mid = (l+r)/2;
        ArrayList<String> ans = new ArrayList<>();
        if (l1 < mid)
        {
            ans.addAll(appendFirstCharactes(encodeInterval(w-1, l, mid, l1, Math.min(mid-1, r1)), '0'));
        }
        if (r1 >= mid)
        {
            ans.addAll(appendFirstCharactes(encodeInterval(w-1, mid, r, Math.max(l1, mid), r1), '1'));
        }
        return ans;
    }

    public static String encodeGray(long a, int w)
    {
        return encodeBinary(a ^ (a >> 1), w);
    }

    public static ArrayList<String> SRGEEncode(int w, long l, long r) {
        return encodeSRGE(w, 0, 1L<<w, l, r, false, 0);
    }

    public static long SRGEEncodeCalc(int w, long l, long r) {
        return encodeSRGECalc(w, 0, 1L<<w, l, r, false, 0);
    }

    public static long encodeCalc(int w, long l, long r, long mode) {
        if (mode == 1) {
            return prefixEncodeCalc(w, l, r);
        }
        if (mode == 2) {
            return  SRGEEncodeCalc(w, l, r);
        }
        if (mode == 3) {
            return r-l+1;
        }
        throw new AssertionError();
    }

    public static ArrayList<String> encodeSRGE(int w, long l, long r, long l1, long r1, boolean fl, int mode)
    {

        if (l == l1 && r == r1+1)
        {
            return new ArrayList<>(Arrays.asList(generatePrefix(w)));
        }

        long mid = (l+r)/2;
        char lc = fl ? '1' : '0';
        char rc = fl ? '0' : '1';

        if (r1 < mid || (mode == 4 && l1 < mid))
        {
            ArrayList<String> ans = appendFirstCharactes(encodeSRGE(w-1, l, mid, l1, Math.min(r1, mid-1), false, mode), lc);
            if (mode == 4) {
                ans.add(rc+generatePrefix(w-1));
            }
            return  ans;
        }

        if (l1 >= mid || (mode == 5 && r1 >= mid))
        {
            ArrayList<String> ans = appendFirstCharactes(encodeSRGE(w-1, mid, r, Math.max(l1, mid), r1, true, mode), rc);
            if (mode == 5) {
                ans.add(lc + generatePrefix(w-1));
            }
            return  ans;
        }


        long inLeft = mid - l1;
        long inRight = r1 - mid+1;

        if (mode == 0) {
            if (inLeft <= inRight) {
                ArrayList<String> ans = appendFirstCharactes(encodeSRGE(w - 1, l, mid, l1, mid-1, false, 4), '*');
                long ll1 = mid + (mid - l1);
                if (ll1 > r1) {
                    return ans;
                }
                ans.addAll(appendFirstCharactes(encodeSRGE(w - 1, mid, r, ll1, r1, true, 3), rc));
                return ans;
            } else {
                ArrayList<String> ans = appendFirstCharactes( encodeSRGE(w - 1, mid, r, mid, r1, true, 5), '*');
                long rr1 = mid - 1 -  (r1 - mid+1);
                ans.addAll(appendFirstCharactes(encodeSRGE(w - 1, l, mid, l1, rr1, false, 2), lc));
                return ans;
            }
        }
        if (mode == 2) {
            if (inLeft < inRight) {
                return encodeSRGE(w, l, r, l1, r1, fl, 4);
            }
            else {
                return appendFirstCharactes(encodeSRGE(w - 1, l, mid, l1, mid-1, false, 4), '*');
            }
        }
        if (mode == 3) {
            if (inLeft > inRight) {
                return encodeSRGE(w, l, r, l1, r1, fl, 5);
            } else {
                return appendFirstCharactes(encodeSRGE(w - 1, mid, r, mid, r1, true, 5), '*');

            }
        }
        throw new AssertionError();
    }


    public static long encodeSRGECalc(int w, long l, long r, long l1, long r1, boolean fl, int mode)
    {

        if (l == l1 && r == r1+1)
        {
            return 1;
        }

        long mid = (l+r)/2;
        if (r1 < mid || (mode == 4 && l1 < mid))
        {
            long ans = encodeSRGECalc(w-1, l, mid, l1, Math.min(r1, mid-1), false, mode);
            if (mode == 4) {
                ans++;
            }
            return  ans;
        }

        if (l1 >= mid || (mode == 5 && r1 >= mid))
        {
            long ans =  encodeSRGECalc(w-1, mid, r, Math.max(l1, mid), r1, true, mode);
            if (mode == 5) {
                ans++;
            }
            return  ans;
        }


        long inLeft = mid - l1;
        long inRight = r1 - mid+1;

        if (mode == 0) {
            if (inLeft <= inRight) {
                long ans = encodeSRGECalc(w - 1, l, mid, l1, mid-1, false, 4);
                long ll1 = mid + (mid - l1);
                if (ll1 > r1) {
                    return ans;
                }
                ans+=encodeSRGECalc(w - 1, mid, r, ll1, r1, true, 3);
                return ans;
            } else {
                long ans = encodeSRGECalc(w - 1, mid, r, mid, r1, true, 5);
                long rr1 = mid - 1 -  (r1 - mid+1);
                ans+=encodeSRGECalc(w - 1, l, mid, l1, rr1, false, 2);
                return ans;
            }
        }
        if (mode == 2) {
            if (inLeft < inRight) {
                return encodeSRGECalc(w, l, r, l1, r1, fl, 4);
            }
            else {
                return encodeSRGECalc(w - 1, l, mid, l1, mid-1, false, 4);
            }
        }
        if (mode == 3) {
            if (inLeft > inRight) {
                return encodeSRGECalc(w, l, r, l1, r1, fl, 5);
            } else {
                return encodeSRGECalc(w - 1, mid, r, mid, r1, true, 5);

            }
        }
        throw new AssertionError();
    }
}
