package com.lv.jdk.basic;

public class FinallyTest {
    public static void main(String[] args) {
        try {
            try {
                try {
                    System.out.println("==========3 try======");
                }finally {
                    int i=1/0;
                    System.out.println("==========3 finally======");
                }
                System.out.println("==========2 try======");
            }finally {
                System.out.println("==========2 finally======");
            }
            System.out.println("==========1 try======");
        }finally {
            System.out.println("==========1 finally======");
        }
    }
}
