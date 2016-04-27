package com.snapdeal.healthcheck.app.constants;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

public class StringUtils {
	private static final String GMAIL_EMAIL_SUFFIX        = "@gmail.com";
    private static final int    GMAIL_EMAIL_SUFFIX_LENGTH = GMAIL_EMAIL_SUFFIX.length();
    public static final String  EMPTY_STRING              = "";

    public static String getRandom(int length) {
        String randomString = getRandom();
        return randomString.substring(randomString.length() - length);
    }

    public static String getRandomAlphaNumeric(int length) {
        String aplhaNumberic = getRandom().toLowerCase().replaceAll("[^\\da-z]", "");
        return aplhaNumberic.substring(aplhaNumberic.length() - length);
    }

    public static String getRandomNumeric(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append((int) (Math.random() * 10));
        }
        return builder.toString();
    }

    public static String getRandom() {
        return UUID.randomUUID().toString();
    }

    public static String generateUserTrackingId() {
        StringBuilder userTrackingId = new StringBuilder();
        userTrackingId.append(String.valueOf(System.currentTimeMillis())).append(StringUtils.getRandomNumeric(5));
        return userTrackingId.toString();
    }

    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        return "".equals(str.trim());
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getAccessorNameForField(String name) {
        return new StringBuffer("get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString();
    }

    public static String getModifierNameForField(String name) {
        return new StringBuffer("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1)).toString();
    }

    public static String getNotNullValue(String value) {
        return value != null ? value : "";
    }

    public static String join(char sep, String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s).append(sep);
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String join(char sep, List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s).append(sep);
        }
        return builder.deleteCharAt(builder.length() - 1).toString();
    }

    public static String normalizeEmail(String email) {
        String emailDomain = "";
        if (email.length() > GMAIL_EMAIL_SUFFIX_LENGTH) {
            emailDomain = email.substring(email.length() - GMAIL_EMAIL_SUFFIX_LENGTH);
        }
        if (emailDomain.equalsIgnoreCase(GMAIL_EMAIL_SUFFIX)) {
            return email.substring(0, email.length() - GMAIL_EMAIL_SUFFIX_LENGTH).replaceAll("\\.", "") + emailDomain;
        } else {
            return email;
        }
    }

    public static Set<String> getEmailIdsAsList(String emails) {
        Set<String> emailList = null;
        if (StringUtils.isNotEmpty(emails)) {
            emailList = new HashSet<String>();
            String[] emailIds = emails.split("[;:, \t\n\r\f]");
            for (int i = 0; i < emailIds.length; i++) {
                String element = emailIds[i];
                if (element.contains("@")) {
                    element = element.replaceAll("[<>,\"]", "");
                    emailList.add(element);
                }
            }
        }
        return emailList;
    }

    public static String getEmailDomain(String email) {
        return email.substring(email.lastIndexOf('@') + 1);
    }

    // get amit.dalal from amit.dalal@xyz.com
    public static String getLocalPartFromEmail(String emailAdd) {
        return emailAdd.substring(0, emailAdd.lastIndexOf('@'));
    }

    public static List<String> split(String input, String regex) {
        return Arrays.asList(input.split(regex));
    }

    public static List<String> split(String input) {
        return split(input, ",");
    }

    // for HDFC Payment gateway
    public static String replaceHackChars(String input) {
        return input.replaceAll("[\\<\\>\\(\\)\\{\\}\\[\\]\\?\\&\\*~`!#$%\\^=\\+\\|:'\\\\,\";]", " ");
    }

    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

    public static void main(String[] args) {
        String aplhaNumberic = getRandom().toLowerCase().replaceAll("[^\\da-z]", "");
        System.out.println(aplhaNumberic.substring(aplhaNumberic.length() - 32));
        System.out.println(parsePrice("1,00.00 Rs").intValue());
    }

    //For JSON-P interaction
    public static String callbackFunction(String functionName, String value) {
        StringBuilder outBuilder = new StringBuilder();
        return outBuilder.append(functionName).append("(").append(value).append(")").toString();
    }

    public static String removeHTML(String input) {
        return input.replaceAll("\\<.*?>", "");
    }

    public static Double parsePrice(String number) {
        number = number.replaceAll("[^\\.\\d,-]", "");
        DecimalFormat df = new DecimalFormat("#,##,###.##");
        Number n;
        try {
            n = df.parse(number);
        } catch (ParseException e) {
            return 0.0;
        }
        return n.doubleValue();
    }

    public static String trim(String value, int endIndex) {
        if (value.length() > endIndex) {
            value = value.substring(0, endIndex - 3).concat("...");
        }
        return value;
    }

    /**
     * Helper function to create a map of the response data from PG. Used by multiple PG's. Creates a map from
     * a1=x1&a2=x2&a3=x3 type string
     * 
     * @param queryString the result string returned from the POST request.
     * @return the map of the response fields returned.
     */
    public static Map<String, String> createMapFromPostResponse(String queryString) {
        Map<String, String> map = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(queryString, "&");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int i = token.indexOf('=');
            if (i > 0) {
                try {
                    String key = token.substring(0, i);
                    //request params sent by PG can't repeat itself. In such case first one shall be considered and other values ignored.
                    // Noticed in case of ICICI PG.
                    if (map.containsKey(key)) {
                        continue;
                    }
                    String value = URLDecoder.decode(token.substring(i + 1, token.length()), "UTF-8");
                    map.put(key, value);
                } catch (Exception ex) {
                    // Do Nothing and keep looping through data
                }
            }
        }
        return map;
    }

    public static String limitNoOfValuesInCookie(String value, String separator, int length) {
        if (isEmpty(value) || isEmpty(separator)) {
            return value;
        }
        String[] arr = value.split(separator);
        if (length > arr.length) {
            return value;
        }
        return join(Arrays.asList(arr).subList(0, length), separator);
    }

    public static String join(List<String> list, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(separator);
        }
        sb.deleteCharAt(sb.length() - separator.length());
        return sb.toString();
    }

}
