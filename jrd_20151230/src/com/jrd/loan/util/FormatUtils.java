package com.jrd.loan.util;

import java.util.regex.Pattern;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 格式检查工具
 *
 * @author Iris
 */
public class FormatUtils {

    public static final Pattern Chinese_Pattern = Pattern.compile("^[\u4E00-\u9FFF]+$");
    public static final Pattern Person_name = Pattern.compile("(([\u4E00-\u9FA5]{2,7})|([a-zA-Z]{2,10}))");
    public static final Pattern Password_Pattern = Pattern.compile("^(?!\\D+$)(?![^a-zA-Z]+$).{6,16}$");
    public static final Pattern NickName_Pattern = Pattern.compile("^[0-9a-zA-Z_-—\u0391-\uFFE5\u4e00-\u9fa5]+$");
    public static final Pattern Phone_Pattern = Pattern.compile("^((14[0-9])|(13[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-3,5-9]))\\d{8}$");
    public static final Pattern Email_Pattern = Pattern.compile("^[a-zA-Z0-9_+.-]+@([a-zA-Z0-9-]+.)+[a-zA-Z0-9]{2,4}$");
    public static final Pattern IDCard15_Pattern = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");// 15位身份证
    public static final Pattern IDCard18_Pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");// 18位身份证
    public static final Pattern IDCard_Pattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
    ;// 身份证
    public static final Pattern URL_Pattern = Pattern
            .compile("(http|https|ftp|Http|Https|Ftp|HTTP|HTTPS|FTP):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
    public static final Pattern Http_or_Https_URL_Pattern = Pattern
            .compile("(http|https|Http|Https|HTTP|HTTPS):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");
    public static final Pattern Character_Pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？] ");

    /**
     * 正则表达式匹配手机号码
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        return Phone_Pattern.matcher(phoneNumber).matches();
    }

    public static boolean isNickname(String nickname) {
        return NickName_Pattern.matcher(nickname).matches();
    }

    /**
     * 正则表达式过滤字符
     *
     * @param character
     * @return
     */
    public static boolean filterCharacter(String character) {
        return Character_Pattern.matcher(character).matches();
    }

    /**
     * 正则表达式匹配邮箱地址
     *
     * @param emailAddr
     * @return
     */
    public static boolean isEmailAddr(String emailAddr) {
        return Email_Pattern.matcher(emailAddr).matches();
    }

    /**
     * 正则表达式匹配http或https地址
     *
     * @param emailAddr
     * @return
     */
    public static boolean isHttpAddr(String httpAddr) {
        return Http_or_Https_URL_Pattern.matcher(httpAddr).matches();
    }

    /**
     * 正则表达式匹配汉字
     *
     * @param string
     * @return
     */
    public static boolean isChineseOnly(String string) {
        return Chinese_Pattern.matcher(string).matches();
    }

    /**
     * 正则表达匹配汉字和字母
     *
     * @param string
     * @return
     */
    public static boolean isPersonName(String string) {
        return Person_name.matcher(string).matches();
    }

    /**
     * 正则表达式匹配身份证号 15位
     *
     * @param emailAddr
     * @return
     */
    public static boolean isIDCard15(String string) {
        return IDCard15_Pattern.matcher(string).matches();
    }

    /**
     * 正则表达式匹配身份证号 18位
     *
     * @param string
     * @return
     */
    public static boolean isIDCard18(String string) {
        return IDCard18_Pattern.matcher(string).matches();
    }

    /**
     * 正则表达式匹配身份证号
     *
     * @param string
     * @return
     */
    public static boolean isIDCard(String string) {
        return IDCard_Pattern.matcher(string).matches();
    }

    /**
     * 正则表达式检查密码复杂度
     *
     * @param password
     * @return
     */
    public static boolean isPwdComplex(String password) {
        return Password_Pattern.matcher(password).matches();
    }

    /**
     * 判断字符串的长度,每个汉字为1,每个字母为1
     *
     * @param string
     * @return
     */
    public static int getStringLengthEn1Cn1(String str) {
        return str.length();
    }

    /**
     * 判断字符串的长度,每个汉字为1,每个字母为0.5
     *
     * @param string
     * @return
     */
    public static int getStringLengthEn0_5Cn1(String str) {
        double num = 0;
        String len;
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            len = Integer.toBinaryString(c[i]);
            if (len.length() > 8) {
                num++;
            } else {
                num += 0.5;
            }
        }
        if (0 == num % 1) {
            return (int) num;
        } else {
            return (int) num + 1;
        }
    }

    /**
     * 判断字符串的长度,每个汉字为1,每个字母为1
     *
     * @param string
     * @return
     */
    public static int getStringLengthEn1Cn2(String str) {
        double num = 0;
        String len;
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            len = Integer.toBinaryString(c[i]);
            if (len.length() > 8) {
                num++;
            } else {
                num += 1;
            }
        }
        if (0 == num % 1) {
            return (int) num;
        } else {
            return (int) num + 2;
        }
    }

    /**
     * edittext 输入小数点后两位
     *
     * @param editText
     */
    public static void setPricePointTwo(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 将字符串中的逗号去掉
     *
     * @param str
     * @return
     */
    public static String ReplaceString(String str) {
        String strValue = "";
        if (str != null && !str.equals("")) {
            strValue = str.replace(",", "");
            LogUtil.d("--------------" + str, "===============" + strValue);
        }
        return strValue;
    }

    /**
     * 输入银行卡每4位 空一格
     *
     * @param editText
     */
    public static void CardEdit(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;
            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = editText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }
                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if (index % 5 == 4) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }
                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }
                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }
                    editText.setText(str);
                    Editable etable = editText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    /**
     * 过滤空格
     */
    public static void inputFilterSpace(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d("11111111111", s.toString());
                if (s.toString().contains(" ")) {
                    s = s.toString().replace(" ", "");
                    edit.setText(s);
                    edit.setSelection(s.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
