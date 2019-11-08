package codility.caterpillar;

import java.util.Arrays;

public class Triangles {
    public static void main(String[] args) {
        //System.out.println(newSol(new int[]{1, 1, 1})); //1
        //System.out.println(newSol(new int[]{10, 2, 5, 1, 8, 12})); //4
        //System.out.println(newSol(new int[]{1, 2, 5, 8, 10, 12})); //4
        System.out.println(solution(new int[]{5, 6, 7, 8, 9})); //10
        //System.out.println(sol(new int[]{5, 6, 7, 8, 9}));
        /**
         * (5,6,7)(5,6,8)(5,7,8)(6,7,8)
         */
    }

    public static int solution(int[] A) {
        if (A.length < 3) return 0;
        Arrays.sort(A);
        //for every possible triangle, check  if it is valid
        int count = 0;
        for (int i=0; i < A.length - 2; i++) {
            int leftEnd = i + 1;
            int rightEnd = i + 2;
            while (leftEnd < A.length - 1) {
                if (rightEnd < A.length && A[i] + A[leftEnd] > A[rightEnd]) {
                    rightEnd++;
                } else {
                    count += (rightEnd - leftEnd - 1);
                    leftEnd++;
                }
            }
        }
        return count;
    }

}
