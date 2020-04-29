import javafx.util.Pair;

import java.util.ArrayList;

public class LZVarhiver {
    public static void codingFile(){
        Dictionary.makeBaseDictionary();


    }
    public static ArrayList<Pair<Character,Integer>> MakeDictionaryBigger(String input){
        ArrayList<Pair<String,Integer>> start= Dictionary.makeBaseDictionary();
        ArrayList<Pair<Character,Integer>> adding = new ArrayList<>();
        ArrayList<Character> in = new ArrayList<>();    //+
        coddingClass.makeDict(input);                   //+
        for(int i=0;i<input.length();i++)
            in.add(input.charAt(i));
        System.out.println(in);

        String currentAdding = "";
        Boolean isInDict=false;
        do{
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
                System.out.println(start.get(i).getKey());
                if(start.get(i).getKey().equals(currentAdding)) {
                    isInDict = true;
                }
            }
        }while (!in.isEmpty());
        System.out.println(start);
        System.out.println(Dictionary.getCurrentSize());

        return adding;
    }
    
}
