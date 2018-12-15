package com.huji.cse.flatfinder.Parser;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private Pattern pricePattern;
    private Pattern addressPattern;
    private Pattern noRoommatesPattern;
    private Pattern numberPattern;
    private Pattern stringPattern;

    public Parser(){
        pricePattern=Pattern.compile("(p|P)rice");
        addressPattern=Pattern.compile("(a|A)ddress");
        noRoommatesPattern =Pattern.compile("(of)* *(r|R)oom(m)?ates");
        numberPattern=Pattern.compile("\\d+");
        stringPattern=Pattern.compile("(?:\\w*\\s*)*");

    }

    public void parse(JSONObject allPosts) throws JSONException {
        //todo change all to string?

        Iterator<String> postsIter = allPosts.keys();
        while (postsIter.hasNext()) {
            String key = postsIter.next();
            if (allPosts.get(key) instanceof JSONObject) {
                JSONObject post = (JSONObject) allPosts.get(key);


                String postId, fullMessage, userName, userNameID, picture, address;
                long price = -1, numOfRoomates = -1;
                float GPSlat, GPSlong;
                boolean favorites = false;
                long created_time;

                String linkToPost = post.getString("link");
                created_time = post.getLong("created_time");
                postId = post.getString("id");

                fullMessage = post.getString("message");

                Matcher matcher = pricePattern.matcher(fullMessage);
                price = getLongField(fullMessage, matcher);

                matcher = noRoommatesPattern.matcher(fullMessage);
                numOfRoomates = getLongField(fullMessage, matcher);

                address=getAddress(fullMessage);

                String nameString = "";
                if (post.has("name")) {
                    nameString = post.getString("name");
                }

                if(post.has("full_picture")){
                    picture=post.getString("full_picture");
                }
            }

        }
    }

    private String getAddress(String fullMessage) {
        Matcher addressMatcher=addressPattern.matcher(fullMessage);
        String address = "";
        if(addressMatcher.find()){
            address=fullMessage.substring(addressMatcher.end());
            Matcher stringMatcher=stringPattern.matcher(address);
            if(stringMatcher.find()) {
                address.substring(stringMatcher.start(), stringMatcher.end());
            }
        }
        return address;
    }


    /**
     * finds the values of all fields that have a numerical value (such as price,num of roommates..)
     * @param text the message text
     * @param fieldMatcher the matcher corresponding to the desired field
     * @return the value of the numerical field.
     */
    private long getLongField(String text, Matcher fieldMatcher) {
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

//    public static void main(String[] args){
//        String text="looking for a new roommate!\\nprice:2143 nis\\naddress: rothschild 12 tel aviv\\nnumber of roommates: 3\\ncome join our amazing apartment :)";
//        Parser parser=new Parser();
//        Matcher m=parser.noRoommatesPattern.matcher(text);
//        long price=parser.getLongField(text,m);
//        System.out.println(price);
//    }
}
