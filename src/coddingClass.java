import java.util.ArrayList;

import static java.lang.Math.pow;

public class coddingClass {
    private static Integer numOfBit = 128;
    public static ArrayList<ArrayList<Byte>> dictionary = new ArrayList<>();
    private static String length = "#000";

    public static String makeDict(String input) {
        String ans = "";
        ArrayList<Byte> inp = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            inp.add((byte) input.charAt(i));
        }
        System.out.println("array byte" + inp);
        for (int i = 0; i < inp.size(); i++)
            if (inp.get(i) > getNumOfBit()) {     //setting num bit of dictionary and length of every name
                doubleNumOfBit();
                moreLength();
            }
        ArrayList<Byte> currentCode = new ArrayList<>();
        ArrayList<String> byteValueDictionary = new ArrayList<>();
        currentCode.add(inp.get(0));
        addArray(dictionary,currentCode);
        inp.remove(0);
        do {
            // System.out.println(inp);
            Boolean isAdd = false;
            for (int i = 0; i < dictionary.size(); i++) {
                //System.out.print(currentCode + "dfgdfh");
                //System.out.println(dictionary.get(i));
                if (sr(currentCode, dictionary.get(i))) {
                    isAdd = true;
                    break;
                }
                //System.out.println(isAdd);
            }
            if (!isAdd) {
                addArray(dictionary,currentCode);
                currentCode.clear();
                currentCode.add(inp.get(0));
                inp.remove(0);
               // System.out.println("wrrgwrg");
            } else {
                currentCode.add(inp.get(0));
                inp.remove(0);
               // System.out.println("121212");
            }
        } while (inp.size() != 0);

        System.out.println(dictionary);

        return ans;
    }

    public static Integer getNumOfBit() {
        return numOfBit;
    }

    public static void doubleNumOfBit() {
        numOfBit *= 2;
    }

    public static void moreLength() {
        length += "0";
    }

    public static ArrayList<Byte> getByteFromString(String in) {
        ArrayList<Byte> ans = new ArrayList<>();
        for (int i = 0; i < in.length(); i++) {
            ans.add((byte) in.charAt(i));
        }
        return ans;
    }

    public static Boolean sr(ArrayList<Byte> first, ArrayList<Byte> second) {
        if (first.size() != second.size())
            return false;
        for (int i = 0; i < first.size(); i++) {
            if (first.get(i) != second.get(i))
                return false;
        }
        return true;
    }
    public static void addArray(ArrayList<ArrayList<Byte>> first,ArrayList<Byte> second){
        ArrayList<Byte> buff = new ArrayList<>();
        for(int i=0;i<second.size();i++){
            buff.add(second.get(i));
        }
        first.add(buff);
    }
    
}