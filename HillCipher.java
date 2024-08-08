import java.util.Scanner;

public class HillCipher {

    // Method to convert character to integer
    public static int charToInt(char c) {
        return c - 'A';
    }

    // Method to convert integer to character
    public static char intToChar(int i) {
        return (char) (i + 'A');
    }

    // Method to multiply matrices
    public static int[] matrixMultiply(int[][] keyMatrix, int[] textVector) {
        int[] result = new int[textVector.length];
        for (int i = 0; i < keyMatrix.length; i++) {
            result[i] = 0;
            for (int j = 0; j < keyMatrix[i].length; j++) {
                result[i] += keyMatrix[i][j] * textVector[j];
            }
            result[i] = result[i] % 26;
        }
        return result;
    }

    // Method to find the modular inverse of a number under modulo 26
    public static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if (((a % m) * (x % m)) % m == 1) {
                return x;
            }
        }
        return 1;
    }

    // Method to find the inverse of the key matrix
    public static int[][] inverseMatrix(int[][] keyMatrix) {
        int determinant = keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0];
        determinant = determinant % 26;
        if (determinant < 0) {
            determinant += 26;
        }

        int inverseDeterminant = modInverse(determinant, 26);

        int[][] inverseMatrix = {
                { keyMatrix[1][1] * inverseDeterminant % 26, (-keyMatrix[0][1] + 26) * inverseDeterminant % 26 },
                { (-keyMatrix[1][0] + 26) * inverseDeterminant % 26, keyMatrix[0][0] * inverseDeterminant % 26 }
        };

        return inverseMatrix;
    }

    // Method to encrypt the plaintext
    public static String encrypt(String plaintext, int[][] keyMatrix) {
        plaintext = plaintext.toUpperCase();
        int[] textVector = new int[keyMatrix.length];
        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += keyMatrix.length) {
            for (int j = 0; j < keyMatrix.length; j++) {
                textVector[j] = charToInt(plaintext.charAt(i + j));
            }

            int[] cipherVector = matrixMultiply(keyMatrix, textVector);

            for (int j = 0; j < cipherVector.length; j++) {
                ciphertext.append(intToChar(cipherVector[j]));
            }
        }
        return ciphertext.toString();
    }

    // Method to decrypt the ciphertext
    public static String decrypt(String ciphertext, int[][] keyMatrix) {
        ciphertext = ciphertext.toUpperCase();
        int[] textVector = new int[keyMatrix.length];
        StringBuilder plaintext = new StringBuilder();

        int[][] inverseKeyMatrix = inverseMatrix(keyMatrix);

        for (int i = 0; i < ciphertext.length(); i += keyMatrix.length) {
            for (int j = 0; j < keyMatrix.length; j++) {
                textVector[j] = charToInt(ciphertext.charAt(i + j));
            }

            int[] plainVector = matrixMultiply(inverseKeyMatrix, textVector);

            for (int j = 0; j < plainVector.length; j++) {
                plaintext.append(intToChar(plainVector[j]));
            }
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the size of the key matrix (e.g., 2 for 2x2 matrix): ");
        int size = sc.nextInt();
        
        int[][] keyMatrix = new int[size][size];

        System.out.println("Enter the key matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                keyMatrix[i][j] = sc.nextInt();
            }
        }

        sc.nextLine(); // consume the newline

        System.out.print("Enter the plaintext (length must be a multiple of " + size + "): ");
        String plaintext = sc.nextLine();

        // Encrypt
        String encryptedText = encrypt(plaintext, keyMatrix);
        System.out.println("Encrypted Text: " + encryptedText);

        // Decrypt
        String decryptedText = decrypt(encryptedText, keyMatrix);
        System.out.println("Decrypted Text: " + decryptedText);

        sc.close();
    }
}
