package com.domain.my.giuseppe.kiu.model;

import java.util.StringTokenizer;

/**
 * Created by giuseppe on 18/08/16.
 */
public class User
{
    /**
     * Empty constructor.
     */
    public User() {}

    public static String getUserName(String UserEmail)
    {
        String userName = "";
        String email = UserEmail;

        StringTokenizer str = new StringTokenizer(email,"@");
        userName = str.nextToken();

        String transformedUsername = "";

        for(int i=0; i<userName.length(); i++)
        {
            if(userName.charAt(i)=='.')
                transformedUsername += ",";
            else
                transformedUsername += userName.charAt(i);
        }
        return transformedUsername;
    }


}
