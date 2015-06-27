package com.mindgames.dailylaw.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

//Notice how we specified the name of the table below
@Table(name = "FAQ")
public class FAQ extends Model {

    // Notice how we specified the name of our column here

    @Column(name = "Question")
    public String Question;

    @Column(name = "Answer")
    public String Answer;

    @Column(name = "Id")
    public int Id;

    @Column(name = "Type")
    public int Type;

    @Column(name = "Category")
    public int Category;

    public FAQ() {
        // Notice how super() has been called to perform default initialization
        // of our Model subclass
        super();
    }

    public static List<FAQ> getFAQs(int Type, int category){
        return new Select()
                .from(FAQ.class)
                .where("Type = ? and Category=?", Type, category)
                .execute();

    }
}