package codility.signal;

public class NameSurname {
    public static void main(String[] args) {
        System.out.println(solution("Brendan", "Weinstein", 27));
        System.out.println(solution("John", "Firelord", 8));
    }

    /**
     * Returns null if inputs are invalid
     * @param name
     * @param surname
     * @param age
     * @return
     */
    public static String solution(String name, String surname, int age) {
        if (name == null || surname == null) throw new IllegalStateException("name and surname must be non-null");
        if (name.length() < 2 || name.length() > 200 || surname.length() < 2 || surname.length() > 20) {
            throw new IllegalStateException("name and surname should have a length between 2 and 20");
        }
        if (age < 1 || age > 200) throw new IllegalStateException("age should be between 1 and 200");
        StringBuilder sb = new StringBuilder();
        sb.append(name, 0, 2);
        sb.append(surname, 0, 2);
        sb.append(age);
        return sb.toString();
    }
}