import java.util.ArrayList;

public class BitArray implements Comparable {

    private String value = ""; //Binary string in Big-Endian

    BitArray(Integer wordLength, Integer integer) {
        int pw = 1;
        for (int i = 0; i < wordLength; i++) {
            pw *= 2;
        }

        for (int i = 0; i < wordLength; i++) {
            if (integer >= pw) {
                value += "1";
                integer -= pw;
            } else {
                value += "0";
            }
            pw /= 2;
        }
    }

    public String getValue() {
        return value;
    }

    public static String parse(String input){
        String output = new String();
        ArrayList<Integer> in = new ArrayList<>();
        ArrayList<Integer> parsed = new ArrayList<>();
        for(int i=0;i<input.length();i++) {
            if(input.charAt(i)!=' ')
                in.add(Integer.parseInt(input.charAt(i)+""));
        }
        int current=0;
        ArrayList<String> binaryTable=new ArrayList<>();
                String currentByte = ""+in.get(0);
            do{
                boolean isInTable = false;
                for(int i=0;i<binaryTable.size();i++){
                    if(currentByte.equals(binaryTable.get(i).toString()))
                        isInTable= true;
                }

                if(isInTable){
                    currentByte+=in.get(current);
                    current++;
                }if(!isInTable){
                    binaryTable.add(currentByte);
                    current++;
                    currentByte=""+in.get(current);
                    isInTable=false;
                }
                //System.out.print(currentByte);
                //System.out.println(binaryTable);
        }while(current!=in.size());
            for(String q:binaryTable)
                output+=q+" ";
                return output;
            }

    @Override
    public int compareTo(Object o) {
        BitArray ob = (BitArray) o;

        if (ob.getValue() == this.value)
            return 0;
        if (ob.getValue().length() < this.value.length())
            return -1;
        if (ob.getValue().length() > this.value.length())
            return 1;
        return ob.getValue().compareTo(this.value);
    }
}
