package com.wildfirechat.wallet.utility;

public class WalletUtility {

	/*	public static void main(String[] args) {
		System.out.println(WalletUtility.maskCardNumber("1234123412345678"));
	}*/
	
	public static String maskCardNumber(String cardNumber) {
		int i = 0;
	    StringBuffer temp = new StringBuffer();
	    while(i < (cardNumber .length())) {
	        if(i > cardNumber .length() -5) {
	            temp.append(cardNumber.charAt(i));
	        } else {
	            temp.append("X");
	        }
	        i++;
	    }
	     return temp.toString();
	}
	
}
