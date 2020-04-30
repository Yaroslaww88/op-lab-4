import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Map;
 class Dictionary {
   private static ArrayList<Pair<String,Byte>> dictionary=new ArrayList<>();
   private static Integer currentSize = 128;

   public static void addDictionary (Pair<String,Byte> input){
       dictionary.add(input);
   }
   public static void addDictionary (String one,Byte input){
       Pair<String,Byte> of = new Pair<>(one+"",input);
        dictionary.add(of);
   }
   public static ArrayList<Pair<String, Byte>> makeBaseDictionary(){
       for(int i=0;i<currentSize;i++){
           Integer one = i;
           addDictionary((char)i+"",one.byteValue());
       }
       System.out.println(dictionary);
       return dictionary;
   }
   public static void doubleCurrentSize(){
       currentSize*=2;
   }
   public static void setDictionary(ArrayList<Pair<String,Byte>> in){
       dictionary = in;
   }
     public static void addDictionary (ArrayList<Pair<String,Byte>> in){
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
