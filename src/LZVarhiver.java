import javafx.util.Pair;

import java.util.ArrayList;

public class LZVarhiver {
    public static void codingFile(){
        Dictionary.makeBaseDictionary();


    }
    public static ArrayList<Pair<Character,Integer>> MakeDictionaryBigger(String input){
        ArrayList<Pair<String,Integer>> start= Dictionary.makeBaseDictionary();
        String inputForCodedByte = input;
        ArrayList<Pair<Character,Integer>> adding = new ArrayList<>();
        ArrayList<Integer> codedByte = new ArrayList<>();
        ArrayList<Character> in = new ArrayList<>();    //+
        coddingClass.makeDict(input);                   //+
        for(int i=0;i<input.length();i++)
            in.add(input.charAt(i));
        //System.out.println(in);
        String currentAdding = "";
        Boolean isInDict=false;
        do{
            Integer sizeOfWord =0;
            for(int i = start.size()-1;i>=0;i--){
                sizeOfWord = start.get(i).getKey().length();
                String buff = "";
                if(sizeOfWord<=inputForCodedByte.length())
                for(int j=0;j<sizeOfWord;j++) {
                    buff += inputForCodedByte.charAt(j);
                }
                else
                    for(int j=0;j<inputForCodedByte.length();j++) {
                        buff += inputForCodedByte.charAt(j);
                    }
               // System.out.println(start);
                //System.out.println(sizeOfWord);
                //System.out.println(codedByte);
                if(buff.equals(start.get(i).getKey())) {
                    codedByte.add(start.get(i).getValue());
                    for (int k=0;k<sizeOfWord;k++)
                        inputForCodedByte=removefirst(inputForCodedByte);
                   // System.out.println(inputForCodedByte);
                    break;
                }
            }

            if(isInDict==false&& currentAdding!="") {
                if(start.size()>=Dictionary.getCurrentSize())
                    Dictionary.doubleCurrentSize();
                start.add(new Pair<>(currentAdding, start.size()));
                currentAdding="";
            }
            if(currentAdding == ""||isInDict==true) {
                currentAdding += in.get(0);
                in.remove(0);
                isInDict=false;
            }
            for(int i=0;i<Dictionary.getDictionarySize();i++){
                //System.out.println(start.get(i).getKey());
                if(start.get(i).getKey().equals(currentAdding)) {
                    isInDict = true;
                }
            }
        }while (!in.isEmpty());
        System.out.println(start);
        System.out.println(codedByte);
       // System.out.println(Dictionary.getCurrentSize());
        return adding;
    }
    public static String removefirst (String in){
        String buf = new String();
        for(int i=1;i<in.length();i++)
            buf+=in.charAt(i);
        return buf;
    }
}
