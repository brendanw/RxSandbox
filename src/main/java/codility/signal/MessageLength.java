package codility.signal;

public class MessageLength {
    public static void main(String[] args) {
        System.out.println(solution("SMS messages are really short", 12)); //3
        System.out.println(solution("hi world", 2));  //1
        System.out.println(solution("hello world", 2)); //0
        System.out.println(solution("hi her hi hi", 4)); //4
        System.out.println(solution(null, 3)); //0
        System.out.println(solution("abc", 0)); //0
        System.out.println("----------");
        System.out.println(solution("hi", 2)); //1
        System.out.println(solution("hi ", 2)); //1
        System.out.println(solution("hi hi ", 2)); //2
        System.out.println(solution("h h h h h", 1)); //5
        System.out.println(solution("ha ha ha ha ha", 2)); //5
        System.out.println("----------");
        System.out.println(solution("ha ha ha", 3)); //3
        System.out.println(solution("h ", 1)); //1
        System.out.println(solution("h ", 2)); //1
        System.out.println(solution("h ", 3)); //1
    }

    public static int solution(String S, int K) {
        if (S == null) return 0;
        int msgCount = 0;
        int index = 0;
        int msgLength = 0;
        int endOfLastWord = -1;
        while (index < S.length()) {
            if (msgLength == K && endOfLastWord == -1 && S.charAt(index) != ' ') {
                //We have a word longer than K. We stop sending messages here.
                return msgCount;
            } else if (msgLength == K && S.charAt(index) != ' ')  {
                // We need to remove the last word to send a message within size limit.
                msgCount++;
                msgLength = 0;
                index = endOfLastWord + 2; //we want to start parsing at the first letter of the next word
                if (index == S.length())  return msgCount;
            } else if (S.charAt(index) == ' ' && msgLength == K) {
                // End of word perfectly aligns with message size limit. Send it.
                msgCount++;
                msgLength = 0;
                endOfLastWord = -1;
                index++;
            } else if (S.charAt(index) == ' ') {
                // Remember where the last word is
                endOfLastWord = index - 1;
                index++;
                msgLength++;
            } else {
                index++;
                msgLength++;
            }
            if (index == S.length() && msgLength <= K && msgLength > 0) {
                msgCount++;
            }
        }
        return msgCount;
    }
}
