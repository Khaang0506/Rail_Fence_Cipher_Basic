/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package railfence;

import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author Khang's PC
 */
public class RunningServer implements Runnable{
    private Scanner in = null;
    private PrintWriter out = null;
    private Socket socket;
    private String name;
    public static final int KHONGLALENH=0;
    public static final int MAHOA=1;
    public static final int GIAIMA=2;
    public static final int THOAT=3;
    public static final int PORT=1234;
    
    public RunningServer(Socket socket, String name) throws  IOException{
        this.socket = socket;
        this.name = name;
        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        new Thread(this).start();
    }
    
    public static int laLenh(String com){
        if(com.equals("MAHOA"))
            return MAHOA;
        if(com.equals("GIAIMA"))
            return GIAIMA;
        return KHONGLALENH;
    }
    
    public void run(){
        try {
            ServerSocket s = new ServerSocket(PORT);
            while(true){
                Socket sv = s.accept();
                boolean lap = true;
                while(lap){
                    String com;
                    Scanner sc = new Scanner(sv.getInputStream());
                    com = sc.nextLine();
                    switch (laLenh(com)) {
                        case MAHOA:
                            //sc.useDelimiter("@");
                            String chuoi = sc.next();
                            int khoa = sc.nextInt();
                            String mahoa;
                            mahoa = encryption(chuoi, khoa);
                            char sec = getSecondChar(chuoi);
                            String tra = mahoa+"@"+sec;
                            out.println(mahoa);
                            out.flush();
                            break;
                        case GIAIMA:
                            
                            break;
                        case THOAT:
                            lap=false;
                            break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(name+" đã khởi hành!...");
        }
    }
    
    public static String encryption(String str, int rails) {
        boolean checkdown = false;  //check whether it is moving downward or upward
        int j = 0;
        int row = rails;                  // no of row is the no of rails entered by user
        int col = str.length();             //column length is the size of string
        char[][] a = new char[row][col];
        //we create a matrix of a of row *col size

        for(int i = 0; i < col; i++){ 
            if (j == 0 || j == row - 1) {
                checkdown = !checkdown;
            }
            a[j][i] = str.charAt(i);
            if (checkdown) {
                j++;
            } else {
                j--;
            }
        }

        for(int i = 0; i < row; i++){
            for (int k = 0; k < col; k++) {
                System.out.print(a[i][k] + "  ");
            }
            System.out.println();
        }
        String en = "";

        for(int i = 0; i < row; i++){
            for (int k = 0; k < col; k++) {
                if (a[i][k] != 0) {
                    en = en + a[i][k];
                }
            }
        }
        return en;
    }
    
    public static String decryption(String str, int rails) {
        boolean checkdown = false;
        int j = 0;
        int row = rails;
        int col = str.length();
        char[][] a = new char[row][col];

        for(int i = 0; i < col; i++){
            if (j == 0 || j == row - 1) {
                checkdown = !checkdown;
            }
            a[j][i] = '*';
            if (checkdown) {
                j++;
            } else {
                j--;
            }
        }
        int index = 0;

        for(int i = 0; i < row; i++){
            for (int k = 0; k < col; k++) {
                if (a[i][k] == '*' && index < str.length()) {
                    a[i][k] = str.charAt(index++);
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int k = 0; k < col; k++) {
                System.out.print(a[i][k] + "\t");
            }
            System.out.println();
        }
        checkdown = false;
        String s = "";
        j = 0;

        for (int i = 0; i < col; i++) {
            if (j == 0 || j == row - 1) {
                checkdown = !checkdown;
            }
            s += a[j][i];
            if (checkdown) {
                j++;
            } else {
                j--;
            }
        }
        return s;
    }

    static char getSecondChar(String str){
        str = str.toLowerCase();
        if (str == null) {
            return '\0';
        }

        int[] ctr = new int[256];
        int i;
        for (i = 0; i < str.length(); i++)
            ctr[str.charAt(i)]++;

        int ctr_first = 0, ctr_second = 0;
        for (i = 0; i < 256; i++) {
            if (ctr[i] > ctr[ctr_first]) {
                ctr_second = ctr_first;
                ctr_first = i;
            } else if (ctr[i] > ctr[ctr_second] && ctr[i] != ctr[ctr_first])
                ctr_second = i;
        }
        char sec = (char) ctr_second;
        return sec;
    }
}
