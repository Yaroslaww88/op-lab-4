import java.awt.geom.Area;
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
        ArrayList<Byte> startArray = new ArrayList<>();
            for(Byte q: inp){
                boolean shouldAdd = true;
                for(int i=0;i<startArray.size();i++){
                    if(q==startArray.get(i)){
                        shouldAdd=false;
                    }
                }
                if (shouldAdd){
                    startArray.add(q);
                    shouldAdd = true;
                }
            }
            addByEllements(dictionary,startArray);
        ArrayList<Byte> currentCode = new ArrayList<>();
            // ArrayList<String> byteValueDictionary = new ArrayList<>();
        //not yusing now
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
            if (!isAdd&&!currentCode.isEmpty()) {
                //System.out.println(currentCode);
                addArray(dictionary,currentCode);
                currentCode= removeWithoutLast(currentCode);
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
        ArrayList<ArrayList<String>> binaryDictionary = new ArrayList<>();
        binaryDictionary= byteToBinary(dictionary);
        System.out.println(binaryDictionary);
        //MakeBinary(dictionary);
        return ans;
    }
    public static String MakeBinary(ArrayList<ArrayList<Byte>> dictionary){
        String ans="";
        String currentCode="#000";
        Integer currentSize = dictionary.get(0).size();
        do{
            /* System.out.println(dictionary); */
            if(dictionary.get(1).size()>currentSize) {
                ans+=currentCode;
                currentCode += "1";
                currentSize=dictionary.get(1).size();
            }
            else
            ans+=currentCode;
            dictionary.remove(0);
        }while(dictionary.size()!=1);
        ans+=currentCode;
        System.out.println(ans);
        return ans;
    }
    public static ArrayList<ArrayList<String>> byteToBinary(ArrayList<ArrayList<Byte>> in){
        ArrayList<ArrayList<String>> out=new ArrayList<>();
        do{
            ArrayList<String>buuf=new ArrayList<>();
            ArrayList<Byte> buff = in.get(0);
            for(int i=0;i<buff.size();i++)
            buuf.add( Main.getBinaryFromByte(buff.get(i)));
            in.remove(0);
            out.add(buuf);
        }while (!in.isEmpty());
        return out;
    }


    public static  ArrayList<Byte> removeWithoutLast(ArrayList<Byte>input){
        do{
            input.remove(0);
        }while (input.size()!=1);
        return input;
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

    public static String makeCodedMessage(ArrayList<ArrayList<Byte>> dictionary){
        String ans = "";


        return ans;
    }
    public static void addByEllements(ArrayList<ArrayList<Byte>> dictionary,ArrayList<Byte> start){
        for(Byte q:start){
            ArrayList<Byte> buff = new ArrayList<>();
            buff.add(q);
            dictionary.add(buff);
        }
    }

}
