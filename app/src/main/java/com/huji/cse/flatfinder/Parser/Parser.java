package com.huji.cse.flatfinder.Parser;


import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;

import com.huji.cse.flatfinder.db.entity.FacebookPost;
import com.huji.cse.flatfinder.viewmodel.PostViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class Parser extends AppCompatActivity {
    private static final String idKey="id";
    private static final String createdTimeKey="created_time";
    private static final String messageKey="message";
    private static final String nameKey="name";
//    private static final String pictureKey="full_picture";

    /**
     * parsers through all of the groups' listing and creates a facebook post object for the database
     * @param allPosts - all of the posts in the group
     * @throws JSONException
     */
    public static void parse(JSONObject allPosts, PostViewModel mViewModel, Geocoder geocoder) throws JSONException {
        JSONArray postsArray = allPosts.getJSONArray("data");
        for (int i = 0; i < postsArray.length() - 1; i++) {
            JSONObject post = (JSONObject) postsArray.get(i);
            String postId = post.getString(idKey);
            /*checks if the post exists in the database*/
            if( !post.has(messageKey) || mViewModel.isPostExists(postId) != 0)
                continue;
            ArrayList<String> images=getImages(post);
            String fullMessage = post.getString(messageKey);
            String createdTime = post.getString(createdTimeKey);
            String address = getAddress(fullMessage);
            double[] gpsCoord = getCoordinate(geocoder, address);

            String userName="";
            if (post.has(nameKey)) {
                userName = post.getString(nameKey);
            }

            long price, numOfRoommates;
            Matcher matcher = ParserPatterns.pricePattern.matcher(fullMessage);
            price = getLongField(fullMessage, matcher);

            matcher = ParserPatterns.noRoommatesPattern.matcher(fullMessage);
            numOfRoommates = getLongField(fullMessage, matcher);

            createFacebookPostObject(fullMessage, userName, images, address, price, numOfRoommates,
                    createdTime, postId, mViewModel,gpsCoord[0],gpsCoord[1]);
        }
    }

    private static ArrayList<String> getImages(JSONObject post) throws JSONException {
        ArrayList<String> output=new ArrayList<String>();
        if(!post.has("attachments"))
            return output;

        JSONObject attachments=post.getJSONObject("attachments");
        JSONObject temp=attachments.getJSONArray("data").getJSONObject(0);
        JSONArray pictures=temp.getJSONObject("subattachments").getJSONArray("data");
        for(int i=0;i<pictures.length();i++){
            JSONObject temp2=(JSONObject)pictures.get(i);
            JSONObject media=temp2.getJSONObject("media");
            String src =media.getJSONObject("image").getString("src");
            output.add(src);
        }
        return output;
    }


    /**
     * create a gps coordinate from a string address using google geocoder
     * @param geocoder google object for maps
     * @param address the address we want to get the coordinate
     * @return an array of 2 double for gps coordinate (lat,long)
     */
    private static double[] getCoordinate(Geocoder geocoder, String address) {
        Address location = getAddressFromString(address,geocoder);
        double[] GPSCordination = {0,0};
        if (location != null) {
            GPSCordination[0] = location.getLongitude();
            GPSCordination[1] = location.getLatitude();
        }
        return GPSCordination;
    }

    /**
     * create an post object that contain all the relivent information about the user post and sacve
     * it to the local database using a viewmodel
     */
    private static void createFacebookPostObject(String fullMessage, String userName, ArrayList<String> pictures,
                                                 String address, long price, long numOfRommates,
                                                 String createdTime, String postId, PostViewModel viewModel,
                                                 double GPSlong, double GPSlat) {
        FacebookPost newPost = new FacebookPost(postId, createdTime, fullMessage, userName,
                null, pictures, GPSlat, GPSlong, price, numOfRommates, false, address);
        viewModel.insert(newPost);
    }
    /**
     * Generates an Address object from a string that represents an address
     * @param addressStr string representation of an address
     * @return Address object that represents addressStr
     */
    private static Address getAddressFromString(String addressStr, Geocoder geocoder) {
        Address address;
        try {
            List<Address> addressList = geocoder.getFromLocationName(addressStr, 5);
            if (addressList != null) {
                address = addressList.get(0);
                address.getLatitude();
                address.getLongitude();
                return address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }




    private static String getUserName(String nameString) {
        Matcher nameMatcher = ParserPatterns.namePattern.matcher(nameString);
        nameMatcher.find();
        return nameMatcher.group(1);
    }

    private static String getAddress(String fullMessage) {
        Matcher addressMatcher = ParserPatterns.addressPattern.matcher(fullMessage);
        String address = "";
        if (addressMatcher.find()) {
            address = fullMessage.substring(addressMatcher.end());
            Matcher stringMatcher = ParserPatterns.stringPattern.matcher(address);
            if (stringMatcher.find()) {
                address = stringMatcher.group(1);
            }
        }
        return address;
    }


    /**
     * finds the values of all fields that have a numerical value (such as price,num of roommates..)
     * @param text         the message text
     * @param fieldMatcher the matcher corresponding to the desired field
     * @return the value of the numerical field.
     */
    private static long getLongField(String text, Matcher fieldMatcher) {
        long output = -1;

        if (fieldMatcher.find()) {
            String fieldSubString = text.substring(fieldMatcher.end());
            Matcher numberMatcher = ParserPatterns.numberPattern.matcher(fieldSubString);
            if (numberMatcher.find()) {
                String numberString = fieldSubString.substring(numberMatcher.start(), numberMatcher.end());
                output = Long.parseLong(numberString);
            }
        }
        return output;
    }


}
