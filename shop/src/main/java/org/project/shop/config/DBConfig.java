package org.project.shop.config;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
public class DBConfig {

    /*
        1. [0, bound-1] 만큼의 난수 범위 생성
        2. (1)의 범위 에서 param1 만큼 곱함
        3. (2)의 범위 에서 param2 만큼 더함
     */
    static List<Integer> createData(int size, int bound, int param1, int param2) {
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int v = (random.nextInt(bound)*param1) + param2;
            numbers.add(v);
        }
        return numbers;
    }
    static List<String> createPublisher(int size, int bound, int param1, int param2) {
        Random random = new Random();
        List<String> publisher = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int v = (random.nextInt(bound)*param1) + param2;
            String pub = "출판사%s".formatted(v);
            publisher.add(pub);
        }
        return publisher;
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
//        int i = sc.nextInt();
        int size = 50;
        List<Integer> prices = createData(size, 500, 100, 10000);
        List<Integer> Quantities = createData(size, 100, 1, 1);
        List<Integer> isbn = createData(size, 9999999, 1, 0);
        List<Integer> pages = createData(size, 340, 1, 100);
        List<String> publishers = createPublisher(size, 10, 1, 1);


        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;

        try{
            br = Files.newBufferedReader(Paths.get("C:\\lee\\Java\\\\BookMarketProject\\data.csv"));
            String line = "";

            while((line = br.readLine()) != null){
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
//                System.out.println(tmpList);
                ret.add(tmpList);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(br != null){
                    br.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        for (List<String> strings : ret) {
            for (String string : strings) {
                System.out.println("string = " + string);
            }
        }

    }
}
