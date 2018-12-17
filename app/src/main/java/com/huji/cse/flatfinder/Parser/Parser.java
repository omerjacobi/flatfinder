package com.huji.cse.flatfinder.Parser;


import com.huji.cse.flatfinder.db.entity.FacebookPost;

import org.json.JSONArray;
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

    public Parser() {
        pricePattern = Pattern.compile("(?:p|P)rice");
        addressPattern = Pattern.compile("(?:a|A)ddress");
        noRoommatesPattern = Pattern.compile("(?:of)* *(?:r|R)oom(?:m)?ates");
        numberPattern = Pattern.compile("\\d+");
        stringPattern = Pattern.compile("((?:\\w+ *)+),*:*-*\\\\*(?:\\n)*");//todo add more delimiters?

    }

    public void parse(JSONObject allPosts) throws JSONException {
        JSONArray postsArray = allPosts.getJSONArray("data");
        for (int i = 0; i < postsArray.length() - 1; i++) {
            JSONObject post = (JSONObject) postsArray.get(i);

            String postId, fullMessage, address, userName = "", picture = "";
            long price, numOfRoommates;
            String createdTime;//todo changed to String
            String linkToPost = "";
            if (post.has("link"))
                linkToPost = post.getString("link");

            createdTime = post.getString("created_time");
            postId = post.getString("id");

            fullMessage = post.getString("message");

            Matcher matcher = pricePattern.matcher(fullMessage);
            price = getLongField(fullMessage, matcher);

            matcher = noRoommatesPattern.matcher(fullMessage);
            numOfRoommates = getLongField(fullMessage, matcher);

            address = getAddress(fullMessage);

            if (post.has("name")) {
                String nameString = post.getString("name");
                userName = getUserName(nameString);
            }

            if (post.has("full_picture")) {
                picture = post.getString("full_picture");
            }

            createFacebookPostObject(fullMessage, userName, picture, address, price, numOfRoommates,
                    createdTime, postId);
        }
    }

    private void createFacebookPostObject(String fullMessage, String userName, String picture,
                                          String address, long price, long numOfRommates,
                                          String createdTime, String postId) {
        //todo OMER - add link to facebook post object, changed time to String
        FacebookPost newPost = new FacebookPost(0, postId, 0, fullMessage, userName,
                null, picture, 0, 0, price, numOfRommates, false, address);

    }

    private String getUserName(String nameString) {
        Pattern namePattern = Pattern.compile("Photos from (.+)'s post");
        Matcher nameMatcher = namePattern.matcher(nameString);
        nameMatcher.find();
        return nameMatcher.group(1);
    }

    private String getAddress(String fullMessage) {
        Matcher addressMatcher = addressPattern.matcher(fullMessage);
        String address = "";
        if (addressMatcher.find()) {
            address = fullMessage.substring(addressMatcher.end());
            Matcher stringMatcher = stringPattern.matcher(address);
            if (stringMatcher.find()) {
                address = stringMatcher.group(1);
            }
        }
        return address;
    }


    /**
     * finds the values of all fields that have a numerical value (such as price,num of roommates..)
     *
     * @param text         the message text
     * @param fieldMatcher the matcher corresponding to the desired field
     * @return the value of the numerical field.
     */
    private long getLongField(String text, Matcher fieldMatcher) {
        long output = -1;

        if (fieldMatcher.find()) {
            String fieldSubString = text.substring(fieldMatcher.end());
            Matcher numberMatcher = numberPattern.matcher(fieldSubString);
            if (numberMatcher.find()) {
                String numberString = fieldSubString.substring(numberMatcher.start(), numberMatcher.end());
                output = Long.parseLong(numberString);
            }
        }
        return output;
    }
}
