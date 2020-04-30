public class Utils {
    /**
     * Calculate word length for given integer (used to expand table)
     * Ex: 1025 - 10
     *     1023 - 9
     * @param integer
     * @return
     */
    public static Integer calcWordLength(Integer integer) {
        int pw = 1;
        // 32 - max word length
        for (int i = 0; i < 32; i++) {
            if (pw >= integer) {
                //System.out.println("calcWordLength: " + integer + " i: " + i);
                return i;
            }
            pw *= 2;
        }
        return 0;
    }

    /**
     * Return binary string with given length
     * @param wordLength - length of output string
     * @param integer - integer to convert to binary
     * @return string
     */
    public static String getBinaryWithGivenWordLength(Integer wordLength, Integer integer) {
        String value = "";
        int pw = 1;
        for (int i = 0; i < wordLength; i++) {
            pw *= 2;
        }

        pw /= 2;

        for (int i = 0; i < wordLength; i++) {
            if (integer >= pw) {
                value += "1";
                integer -= pw;
            } else {
                value += "0";
            }
            pw /= 2;
        }

        return value;
    }


//    /**
//     * Return Integer from binary string
//     * @param s - binary string
//     * @return parsed Integer
//     */
//    public static Integer getIntegerFromBinary(String s) {
//        Integer value = 0;
//        int pw = 1;
//
//        for (int i = 0; i < s.length(); i++) {
//            if (s.charAt(s.length() - i - 1) == '1')
//                value += pw;
//
//            pw *= 2;
//        }
//
//        return value;
//    }

    /**
     * Return SIGNED Byte from binary string
     * @param s - binary string
     * @return parsed Byte
     */
    public static Byte getByteFromBinary(String s) {
        byte value = 0;
        byte pw = 1;

        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(s.length() - i - 1) == '1') {
                value += pw;

            }

            pw *= 2;
        }

        if (s.charAt(0) == '1')
            value *= -1;

        if (s.charAt(0) == '1' && value == 0)
            value = -128;

        return value;
    }

    public static String getBinaryFromByte(byte b) {
        String value = "";
        int pw = 1;
        for (int i = 0; i < 6; i++) {
            pw *= 2;
        }

        if (b < 0) {
            value += "1";
            b *= -1;
        } else
            value += "0";

        for (int i = 0; i < 7; i++) {
            if (b >= pw) {
                value += "1";
                b -= pw;
            } else {
                value += "0";
            }
            pw /= 2;
        }

        return value;
    }
}
