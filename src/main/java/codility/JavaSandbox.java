package codility;

import java.util.ArrayList;

public class JavaSandbox {
    public static void main(String[] args) {
    }

    public static long gcd(long N, long M) {
        if (N % M == 0) return M;
        return gcd (M, N % M);
    }

    public static long lcm(long N, long M) {
        return N * (M / gcd(N, M));
    }

    public static int shortSol(int N, int M) {
        return (int) (lcm(N, M) / M);
    }

    public static int efficientSol(int N, int M) {
        int x = 0;
        x = (x + M) % N;
        int turns = 1;
        while (x != 0) {
            x = (x + M) % N;
            turns++;
        }
        return turns;
    }

    /**
     * neckplace problem
     * @param N
     * @return
     */
    public static int sol2(int[] N) {
        if (N.length == 0) return 0;
        if (N.length == 1) return 1;
        int max = 0;
        for (int i=0; i < N.length; i++) {
            int cursor = N[i];
            if (cursor < i) continue;
            int length = 0;
            while (cursor != i) {
                cursor = N[cursor];
                length++;
            }
            if (N.length % 2 == 0 && length >= N.length / 2) return length;
            if (N.length % 2 == 1 && length > N.length / 2) return length;
            max = Math.max(max, length);
        }
        return max;
    }

    /**
     * light-switch problem
     * @param N
     * @return
     */
    public static int sol(int[] N) {
        int moments = 0;
        int sum = 0;
        int requiredSum = 0;
        for (int i=0; i < N.length; i++) {
            sum += N[i];
            requiredSum += i + 1;
            if (sum == requiredSum) moments++;
        }
        return moments;
    }
}
