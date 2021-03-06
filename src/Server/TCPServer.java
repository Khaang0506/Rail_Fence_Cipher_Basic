/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Khang's PC
 */
public class TCPServer {
    public static final int KHONGLALENH=0;
    public static final int MAHOA=1;
    public static final int GIAIMA=2;
    public static final int THOAT=3;
    public static final int PORT=1234;
    
    public static int laLenh(String com){
        if(com.equals("MAHOA"))
            return MAHOA;
        if(com.equals("GIAIMA"))
            return GIAIMA;
        return KHONGLALENH;
    }
    public static void main(String[] args) {
        ServerSocket s;
        try {
            System.out.println("Server is listening...");
            s = new ServerSocket(PORT);
            while(true){
                Socket sv = s.accept();
                boolean lap = true;
                while(lap){
                    String com;
                    Scanner sc = new Scanner(sv.getInputStream());
                    com = sc.nextLine();
                    String chuoi = sc.next();
                    int khoa = sc.nextInt();
                    PrintWriter out = new PrintWriter(sv.getOutputStream());
                    switch (laLenh(com)) {
                        case MAHOA:
                            System.out.println("Đã vào lệnh mã hoá!...");
                            String mahoa;
                            mahoa = encryption(chuoi, khoa);
                            char sec = getSecondChar(mahoa);
                            int i = getTimeSecondChar(mahoa,sec);
                            out.println(mahoa);
                            out.println(sec);
                            out.println(i);
                            System.out.println(" đã khởi hành!...");
                            out.flush();
                            break;
                        case GIAIMA:  
                            System.out.println("Đã vào lệnh giải mã!...");
                            String giaima;
                            giaima = decryption(chuoi, khoa);
                            char sec2= getSecondChar(giaima);
                            int i2 = getTimeSecondChar(giaima,sec2);
                            out.println(giaima);
                            out.println(sec2);
                            out.println(i2);
                            System.out.println(" đã khởi hành!...");
                            out.flush();
                            break;
                        case THOAT:
                            lap=false;
                            break;
                    }
                    break;
                }
                sv.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static String encryption(String str, int rails) {
        str = str.toLowerCase();
        boolean check = false;//kiểm tra đang đi xuống hay đi lên
        int j = 0;
        int row = rails;
        int col = str.length();
        char[][] a = new char[row][col];

        //lấy các kí tự của chuỗi và xếp vào một ma trận a
        for(int i = 0; i < col; i++){ 
            if (j == 0 || j == row - 1){
                check = !check;
            }
            a[j][i] = str.charAt(i);
            if (check){
                j++;
            } else {
                j--;
            }
        }
        //truy cập ma trận theo thứ tự thông thường để lấy bản mã
        //lưu bản mã thành một chuỗi để xuất
        String en = "";
        for(int i = 0; i < row; i++){
            for (int k = 0; k < col; k++){
                if (a[i][k] != 0) {
                    en = en + a[i][k];
                }
            }
        }
        return en;
    }
    
    public static String decryption(String str, int rails) {
        str = str.toLowerCase();
        boolean check = false;
        int j = 0;
        int row = rails;
        int col = str.length();
        char[][] a = new char[row][col];

        //lấy các kí tự của chuỗi
        for(int i = 0; i < col; i++){
            if (j == 0 || j == row - 1){
                check = !check;
            }
            a[j][i] = '*';//đánh dấu vị trí đường ray bằng * trong ma trận
            if (check){
                j++;
            }else{
                j--;
            }
        }
        
        //bây giờ nhập ký tự của bản mã vào vị trí ma trận có ký hiệu *
        int index = 0;
        for(int i = 0; i < row; i++){
            for (int k = 0; k < col; k++){
                if (a[i][k] == '*' && index < str.length()){
                    a[i][k] = str.charAt(index++);
                }
            }
        }

        check = false;
        String s = "";
        j = 0;

        for (int i = 0; i < col; i++){
            if (j == 0 || j == row - 1){
                check = !check;
            }
            s += a[j][i];
            if (check){
                j++;
            } else {
                j--;
            }
        }
        return s;//in chuỗi đã được giải mã bằng rail fence
    }

    static char getSecondChar(String str){
        int[] count = new int[256];
        int i;
        for (i=0; i< str.length(); i++)
            (count[str.charAt(i)])++;

        int first = 0, second = 0;
        for (i = 0; i < 256; i++)
        {
            if (count[i] > count[first])
            {
                second = first;
                first = i;
            }else if (count[i] > count[second] && count[i] != count[first])
                second = i;
        }
        char sec = (char) second;
        return sec;
    }
    
    static int getTimeSecondChar(String str, char sec){
        int[] count = new int[256];
        int i;
        for (i=0; i< str.length(); i++)
            (count[str.charAt(i)])++;

        for (i=0; i< str.length(); i++){
            if(str.charAt(i)==sec){
                return count[str.charAt(i)];
            }
        }
        return 0;
    }
}
