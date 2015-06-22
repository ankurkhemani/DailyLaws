package com.mindgames.dailylaw.model;

import android.util.Log;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

//Notice how we specified the name of the table below
@Table(name = "LawBook")
public class LawBook extends Model {

    // Notice how we specified the name of our column here

    @Column(name = "SectionNumber")
    public String SectionNumber;

    @Column(name = "SectionParticulars")
    public String SectionParticulars;

    @Column(name = "SectionDisplay")
    public String SectionDisplay;

    @Column(name = "ChapterNumber")
    public Chapters ChapterNumber;

    @Column(name = "Bookmark")
    public int Bookmark;

    @Column(name = "Id")
    public int Id;

    @Column(name = "Type")
    public int Type;

    public LawBook() {
        // Notice how super() has been called to perform default initialization
        // of our Model subclass
        super();
    }

    public LawBook(String SectionNumber, String SectionParticulars, String SectionDisplay, Chapters ChapterNumber, int Bookmark, int Id, int Type) {
        super();
        this.SectionNumber = SectionNumber;
        this.SectionParticulars = SectionParticulars;
        this.SectionDisplay = SectionDisplay;
        this.ChapterNumber = ChapterNumber;
        this.Bookmark = Bookmark;
        this.Id = Id;
        this.Type = Type;
    }

    public static List<LawBook> getChapterRows(int chapterNumber, int Type){
        return new Select()
                .from(LawBook.class)
//                .innerJoin(Chapters.class).on("LawBook.ChapterNumber=Chapters.ChapterNumber")
                .where("ChapterNumber = ? and Type = ?", chapterNumber, Type)
                .execute();
    }

    public static void updateBookmarks(int Id, int Bookmark) {
        LawBook updateBM = LawBook.load(LawBook.class, Id);
        updateBM.Bookmark = Bookmark;
        updateBM.save();
    }

    public static List<LawBook> getBookmarks(int Type){
        return new Select()
                .from(LawBook.class)
                .where("Bookmark = 1 and Type = ?", Type)
                .execute();

    }

    public static List<LawBook> searchQuery(String query){
        return new Select()
                .from(LawBook.class)
                .where("SectionNumber REGEXP '" + query + "[A-Z]' or SectionNumber LIKE '" + query + "'" )
                .execute();

    }

}