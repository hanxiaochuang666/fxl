package com.by.blcu.core.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Description: validate a mobile phone.
 * @author Daniel
 * @date 2019/08/08 17:37
 */
public class PhoneUtil {
	@SuppressWarnings("unused")
	private static final String Default_RegionCode = "CN";
	public static final int Default_CountryCode_Int = 86;

	private static final Pattern GLOBAL_PHONE_NUMBER_PATTERN = Pattern.compile("[\\+]?[0-9.-]+");
	public static boolean isGlobalPhoneNumber(String phoneNumber) {
		Matcher match = GLOBAL_PHONE_NUMBER_PATTERN.matcher(phoneNumber);
		return match.matches();
	}

	private static String MobilenoPattern = "^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|17[0-9]|18[0-9]|19[89]|14[57])[0-9]{8}$";

	public static boolean isValidCNMobilenoCharacter(final String mobileno) {
		Pattern p = Pattern.compile(MobilenoPattern);
		Matcher m = p.matcher(mobileno);
		return m.find();
	}
	
	public static boolean isValidPhoneCharacter(int countryCode,String mobileno){

		int charlen = mobileno.length();
		if(charlen < 6 || charlen > 16){
			return false;
		}
		if(Default_CountryCode_Int == countryCode){
			return isValidCNMobilenoCharacter(mobileno);
		}

		return isGlobalPhoneNumber(mobileno);
	}

    public static String format(int countryCode,String mobileno){
        StringBuilder sb = new StringBuilder();
        if(countryCode == 0){
            sb.append(mobileno);
        }else{
            sb.append(countryCode).append('.').append(mobileno);
        }
        return sb.toString();
    }

}
