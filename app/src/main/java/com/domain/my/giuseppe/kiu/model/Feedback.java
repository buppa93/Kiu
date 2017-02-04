package com.domain.my.giuseppe.kiu.model;

import com.domain.my.giuseppe.kiu.remotedatabase.RemoteDatabaseString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giuseppe on 08/08/16.
 */
public class Feedback
{
    private String feedbackno;
    private String rating;
    private String comment;

    public Feedback () {}

    public Feedback(String feedbackno, String rating, String comment)
    {
        this.feedbackno = feedbackno;
        this.rating = rating;
        this.comment = comment;
    }

    public Feedback(String rating, String comment)
    {
        this.rating = rating;
        this.comment = comment;
    }

    public String getFeedbackno() {return this.feedbackno;}

    public String getRating() {return this.rating;}

    public String getComment() {return this.comment;}

    public void setFeedbackno(String feedbackno) {this.feedbackno = feedbackno;}

    public void setRating(String rating) {this.rating = rating;}

    public void setComment(String comment) {this.comment = comment;}

    public String toString()
    {
        return "Feedbackno: "+this.getFeedbackno()+", Rating: "
                +this.getRating()+", Comment: "+this.getComment();

    }

    public HashMap<String, String> toHashMap()
    {
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(RemoteDatabaseString.KEY_FEEDBACK_COMMENT, this.getComment());
        hashMap.put(RemoteDatabaseString.KEY_FEEDBACK_NO, this.getFeedbackno());
        hashMap.put(RemoteDatabaseString.KEY_FEEDBACK_RATING, this.getRating());

        return hashMap;
    }

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();

        result.put(RemoteDatabaseString.KEY_FEEDBACK_NO, this.feedbackno);
        result.put(RemoteDatabaseString.KEY_FEEDBACK_RATING, this.rating);
        result.put(RemoteDatabaseString.KEY_FEEDBACK_COMMENT, this.comment);

        return result;
    }


}
