package codility.binary;

public class MinMaxDivision {
    public static void main(String[] args) {
        System.out.println(solution(new int[] { 2, 1, 5, 1, 2, 2, 2}, 3)); //6
        System.out.println(solution(new int[] { 200, 1, 1, 1, 1}, 4)); //200
        System.out.println(solution(new int[] { 50, 20, 50, 20, 30}, 3)); //70
        System.out.println(solution(new int[] { 50, 20, 50, 20, 30}, 2)); //100
        System.out.println(solution(new int[] { 50, 20, 50, 20, 30}, 1)); //170
    }

    public static int solution(int[] A, int nBlocks) {
        int lower = max(A);
        int upper = sumAll(A);
        if (nBlocks == A.length) return lower;
        if (nBlocks == 1) return upper;
        int result = upper;
        while (lower <= upper) {
            int mid = (lower + upper) / 2;
            if (isMaxBlockPossible(A, mid,  nBlocks)) {
                result = mid;
                upper = mid - 1;
            } else {
                lower = mid + 1;
            }
        }
        return result;
    }

    public static boolean isMaxBlockPossible(int[] A, int maxSize, int maxBlocks) {
        int sum = 0;
        int blocks = 1;
        for (int i=0; i < A.length; i++) {
            if (A[i] + sum > maxSize) {
                blocks++;
                if (blocks > maxBlocks) return false;
                sum = A[i];
            } else {
                sum += A[i];
            }
        }
        return true;
    }

    public static int max(int[] A) {
        int max = A[0];
        for (int a : A) {
            if (a > max) max = a;
        }
        return max;
    }

    public static int sumAll(int[] A) {
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        return sum;
    }
}
