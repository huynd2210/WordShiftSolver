import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.security.jgss.GSSUtil;

import java.io.*;
import java.util.*;

public class Solver {
    private static StringBuilder addToBinaryString(StringBuilder binary){
        for (int i = 0; i < binary.length(); i++){
            if (binary.charAt(i) == '0'){
                binary.setCharAt(i, '1');
                return binary;
            }
        }
        for (int j = 1; j < binary.length(); j++){
            binary.setCharAt(j, '0');
        }
        return binary;
    }

    public static boolean addWithCarry(StringBuilder binaryString){
        for (int i = 0; i < binaryString.length(); i++){
            if (!isBinaryFull(binaryString)){
                addToBinaryString(binaryString);
                return false;
            }
        }
        addToBinaryString(binaryString);
        return true;
    }

    public static boolean isBinaryFull(StringBuilder binaryString){
        for (int i = 0; i < binaryString.length(); i++){
            if (binaryString.charAt(i) != '1'){
                return false;
            }
        }
        return true;
    }

    public static void addAndCarryUp(List<StringBuilder> input){
        addAndCarryUp(input, 0);
    }

    private static void addAndCarryUp(List<StringBuilder> input, int currentIndex){
        boolean isCarry = addWithCarry(input.get(currentIndex));
        if (isCarry){
            addAndCarryUp(input, currentIndex + 1);
        }
    }

    private static List<StringBuilder> appendBinary(List<String> strings){
        List<StringBuilder> result = new ArrayList<>();
        for (String s : strings){
            StringBuilder binary = new StringBuilder();
            for (int i = 0; i < s.length(); i++){
                binary.append('0');
            }
            binary.setCharAt(0, '1');
            result.add(binary);
        }
        return result;
    }

    public static List<StringBuilder> shiftString(List<String> input){
        List<StringBuilder> result = new ArrayList<>();
        StringBuilder tpm = new StringBuilder();
        for (int i = 0; i < input.size(); i++){
            tpm.append(input.get(i).charAt(0));
        }
        result.add(tpm);

        List<StringBuilder> binary = appendBinary(input);
        boolean full = false;
        while (!full){
            addAndCarryUp(binary);
            tpm = new StringBuilder();
            for (int i = 0; i < input.size(); i++){
                tpm.append(input.get(i).charAt(binary.get(i).lastIndexOf("1")));
            }
            result.add(tpm);
            full = binary.stream().allMatch(Solver::isBinaryFull);
        }
        return result;
    }

    public static List<String> getSolution(List<String> input) throws IOException {
//        HashMap wordMap = new ObjectMapper().readValue(new File("wordMap.txt"), HashMap.class);
        List<StringBuilder> shiftedStrings = shiftString(input);
        List<String> result = new ArrayList<>();
        ArrayList<String> mw = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(new File("wordList.txt")));
        String st;
        while ((st = br.readLine()) != null){
            mw.add(st);
        }
        for (StringBuilder sb : shiftedStrings){
            String candidate = sb.toString();
            if (mw.contains(candidate)){
                result.add(candidate);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Enter the list of strings column wise");
        System.out.println("Enter '-1' to end input phase");
        List<String> tmp = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        String s = "";
        while(!(s = sc.nextLine()).equalsIgnoreCase("-1")){
            tmp.add(s);
            System.out.println("Enter another column or '-1' to find solution");
        }
        System.out.println("Solutions are: ");
        System.out.println(getSolution(tmp));
    }
}
