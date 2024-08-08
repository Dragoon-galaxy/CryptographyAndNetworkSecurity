import java.util.*;

class Playfair {
    String key;
    String plainText;
    char[][] matrix = new char[5][5];

    public Playfair(String key, String plainText) {
        this.key = key.toLowerCase();
        this.plainText = plainText.toLowerCase();
    }

    public void cleanPlayFairKey() {
        LinkedHashSet<Character> set = new LinkedHashSet<>();
        StringBuilder newKey = new StringBuilder();
        for (int i = 0; i < key.length(); i++)
            set.add(key.charAt(i));
        for (Character character : set)
            newKey.append(character);
        key = newKey.toString();
    }

    public void generateCipherKey() {
        Set<Character> set = new HashSet<>();
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == 'j')
                continue;
            set.add(key.charAt(i));
        }
        StringBuilder tempKey = new StringBuilder(key);
        for (int i = 0; i < 26; i++) {
            char ch = (char) (i + 97);
            if (ch == 'j')
                continue;
            if (!set.contains(ch))
                tempKey.append(ch);
        }
        for (int i = 0, idx = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = tempKey.charAt(idx++);
        System.out.println("Playfair Cipher Key Matrix:");
        for (int i = 0; i < 5; i++)
            System.out.println(Arrays.toString(matrix[i]));
    }

    public String formatPlainText() {
        StringBuilder message = new StringBuilder();
        int len = plainText.length();

        for (int i = 0; i < len; i++) {
            if (plainText.charAt(i) == 'j')
                message.append('i');
            else
                message.append(plainText.charAt(i));
        }

        for (int i = 0; i < message.length(); i += 2) {
            if (i + 1 < message.length() && message.charAt(i) == message.charAt(i + 1))
                message.insert(i + 1, 'x');
        }

        if (message.length() % 2 == 1)
            message.append('x');
        return message.toString();
    }

    public String[] formPairs(String message) {
        int len = message.length();
        String[] pairs = new String[len / 2];
        for (int i = 0, cnt = 0; i < len / 2; i++)
            pairs[i] = message.substring(cnt, cnt += 2);
        return pairs;
    }

    public int[] getCharPos(char ch) {
        int[] keyPos = new int[2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == ch) {
                    keyPos[0] = i;
                    keyPos[1] = j;
                    break;
                }
            }
        }
        return keyPos;
    }

    public String encryptMessage() {
        String message = formatPlainText();
        String[] msgPairs = formPairs(message);
        StringBuilder encText = new StringBuilder();
        for (String pair : msgPairs) {
            char ch1 = pair.charAt(0);
            char ch2 = pair.charAt(1);
            int[] ch1Pos = getCharPos(ch1);
            int[] ch2Pos = getCharPos(ch2);
            if (ch1Pos[0] == ch2Pos[0]) {
                ch1Pos[1] = (ch1Pos[1] + 1) % 5;
                ch2Pos[1] = (ch2Pos[1] + 1) % 5;
            } else if (ch1Pos[1] == ch2Pos[1]) {
                ch1Pos[0] = (ch1Pos[0] + 1) % 5;
                ch2Pos[0] = (ch2Pos[0] + 1) % 5;
            } else {
                int temp = ch1Pos[1];
                ch1Pos[1] = ch2Pos[1];
                ch2Pos[1] = temp;
            }
            encText.append(matrix[ch1Pos[0]][ch1Pos[1]])
                   .append(matrix[ch2Pos[0]][ch2Pos[1]]);
        }
        return encText.toString();
    }

    public String decryptMessage(String cipherText) {
        String[] msgPairs = formPairs(cipherText);
        StringBuilder decText = new StringBuilder();
        for (String pair : msgPairs) {
            char ch1 = pair.charAt(0);
            char ch2 = pair.charAt(1);
            int[] ch1Pos = getCharPos(ch1);
            int[] ch2Pos = getCharPos(ch2);
            if (ch1Pos[0] == ch2Pos[0]) {
                ch1Pos[1] = (ch1Pos[1] - 1 + 5) % 5;
                ch2Pos[1] = (ch2Pos[1] - 1 + 5) % 5;
            } else if (ch1Pos[1] == ch2Pos[1]) {
                ch1Pos[0] = (ch1Pos[0] - 1 + 5) % 5;
                ch2Pos[0] = (ch2Pos[0] - 1 + 5) % 5;
            } else {
                int temp = ch1Pos[1];
                ch1Pos[1] = ch2Pos[1];
                ch2Pos[1] = temp;
            }
            decText.append(matrix[ch1Pos[0]][ch1Pos[1]])
                   .append(matrix[ch2Pos[0]][ch2Pos[1]]);
        }
        return decText.toString();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the key:");
        String key = sc.nextLine();
        System.out.println("Enter the plaintext:");
        String plainText = sc.nextLine();

        Playfair pfc = new Playfair(key, plainText);
        pfc.cleanPlayFairKey();
        pfc.generateCipherKey();

        String encText = pfc.encryptMessage();
        System.out.println("Encrypted Text: " + encText);

        System.out.println("\nEnter the cipher text to decrypt:");
        String cipherText = sc.nextLine();
        
        Playfair decryptionPfc = new Playfair(key, "");
        decryptionPfc.cleanPlayFairKey();
        decryptionPfc.generateCipherKey();
        String decText = decryptionPfc.decryptMessage(cipherText);
        System.out.println("Decrypted Text: " + decText);

        sc.close();
    }
}
