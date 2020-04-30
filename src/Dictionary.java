import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;
 class Dictionary {
   private static ArrayList<Pair<String,Integer>> dictionary=new ArrayList<>();
   private static Integer currentSize = 128;

   public static void addDictionary (Pair<String,Integer> input){
       dictionary.add(input);
   }
   public static void addDictionary (String one,Integer input){
       Pair<String,Integer> of = new Pair<>(one+"",input);
        dictionary.add(of);
   }
     public static ArrayList<Pair<String, Integer>> makeBaseDictionary() {
         for (int i = 0; i <= currentSize; i++)
             addDictionary((char) i + "", i);
             System.out.println(dictionary);
             return dictionary;
     }
   public static void doubleCurrentSize(){
       currentSize*=2;
   }
   public static void setDictionary(ArrayList<Pair<String,Integer>> in){
       dictionary = in;
   }
     public static void addDictionary (ArrayList<Pair<String,Integer>> in){
       for(int i=0;i<in.size();i++){
           addDictionary(in.get(i));
         }
     }
     public static Integer getDictionarySize(){
       return dictionary.size();
     }
     public static Integer getCurrentSize(){
       return currentSize;
     }
}
