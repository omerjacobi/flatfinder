package com.huji.cse.flatfinder.Parser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private Pattern pricePattern;
    private Pattern addressPattern;
    private Pattern noRoomatesPattern;
    private Pattern numberPattern;

    public Parser(){
        pricePattern=Pattern.compile("(p|P)rice");
        addressPattern=Pattern.compile("(a|A)ddress");
        noRoomatesPattern=Pattern.compile("(of)* *(r|R)oom(m)?ates");
        numberPattern=Pattern.compile("\\d+");

    }

    public void parse(JSONObject post) throws JSONException {
        //todo change all to string?

        String postId, fullMessage, userName, userNameID, picture, address;
        long price=-1 , numOfRoomates=-1;
        float GPSlat, GPSlong;
        boolean favorites=false;
        long created_time;

        String linkToPost=post.getString("link");
        created_time=post.getLong("updated_time");
        postId=post.getString("id");

        String text=post.getString("message");

        Matcher matcher=pricePattern.matcher(text);
        price=getLongField(text, matcher);

        matcher=noRoomatesPattern.matcher(text);
        numOfRoomates=getLongField(text,matcher);


    }


    /**
     * finds the values of all fields that have a numerical value (such as price,num of roommates..)
     * @param text the message text
     * @param fieldMatcher the matcher corresponding to the desired field
     * @return the value of the numerical field.
     */
    public long getLongField(String text, Matcher fieldMatcher) {
        long output=-1;

        if(fieldMatcher.find()){
            String fieldSubString=text.substring(fieldMatcher.end());
            Matcher numberMatcher=numberPattern.matcher(fieldSubString);
            if(numberMatcher.find()){
                String numberString=fieldSubString.substring(numberMatcher.start(),numberMatcher.end());
                output=Long.parseLong(numberString);
            }
        }
        return output;
    }

    public static void main(String[] args){
        String text="looking for a new roommate!\\nprice:2143 nis\\naddress: rothschild 12 tel aviv\\nnumber of roommates: 3\\ncome join our amazing apartment :)";
        Parser parser=new Parser();
        Matcher m=parser.noRoomatesPattern.matcher(text);
        long price=parser.getLongField(text,m);
        System.out.println(price);
    }
}
