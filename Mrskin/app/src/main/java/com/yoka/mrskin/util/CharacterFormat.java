package com.yoka.mrskin.util;

public class CharacterFormat{
	public static String convert(int d){
		String s=String.valueOf(d);
		String[] str={"零","一","二","三","四","五","六","七","八","九"};//可以吧“一”换成“壹”
		String ss[] = new String[]{"","十","百","千","万"};//数值过大，可以继续加“万”，“十万”....
		StringBuffer sb=new StringBuffer();			
		for(int i=0,j=(s.length()-1);i<s.length();i++,j--){
			String index=String.valueOf(s.charAt(i));
			//遇到零的时候的处理，例如：405
			if(str[Integer.parseInt(index)].equals("零")){
				if(i==(s.length()-1)){
					continue;
				}
				sb=sb.append(str[Integer.parseInt(index)]);
			}else{
				sb=sb.append(str[Integer.parseInt(index)]+ss[j]);
			}
		}
		String numble = sb.toString();
		//清除sb中的字符值
		sb.delete(0, sb.length());
		if( 10 <= d && d < 20){
			return numble.substring(1);
		}
		if(numble.endsWith("零")){
			return numble.substring(0, numble.length()-1);
		}
		if(numble.contains("零零") || numble.contains("零零零") ){
			if(numble.endsWith("零")){
				return numble.replace("零零", "零").replace("零零零", "零").substring(0, numble.length()-1);
			}
			return numble.replace("零零", "零").replace("零零零", "零");
		}else if(numble.endsWith("零")){
			return numble.substring(0, numble.length()-1);
		}
		return numble;
	}
}