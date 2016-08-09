package com.toroot.TextFetch;
public class NaoCan {  
	
   public static void main(String[] args) { 
       //打印上半部分  
	   String x="可";
	   String s="sb";
       for (int i = 0; i < 5; i++) {//最外城循环，控制这部分总共有5行  
           for (int j = 0; j < 5; j++) {  
               if (j == 4 - i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           for (int k = 0; k < 5; k++) {  
               if (k == 4)  
                   continue;  
               if (k == i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           for (int j = 0; j < 5; j++) {  
               if (j == 0)  
                   continue;  
               if (j == 4 - i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           for (int k = 0; k < 5; k++) {  
               if (k == i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           System.out.println();  
       }  
       //打印下半部分  
       for (int i = 0; i < 9; i++) {  
           for (int k = 0; k < 9; k++) {  
               if (k == i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           for (int j = 0; j < 9; j++) {  
               if (j == 8 - i)  
                   System.out.print(x);  
               else  
                   System.out.print(s);  
           }  
           System.out.println();  
       }  
   }  
}  