package com.mindgames.dailylaw.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

//Notice how we specified the name of the table below
@Table(name = "Chapters")
public class Chapters extends Model {

    // Notice how we specified the name of our column here

    @Column(name = "ChapterNumber")
    public int ChapterNumber;

    @Column(name = "IPCChapterDenotion")
    public String IPCChapterDenotion;

    @Column(name = "IPCChapterDescription")
    public String IPCChapterDescription;

    @Column(name = "IPCSectionRange")
    public String IPCSectionRange;

    @Column(name = "CrPCChapterDenotion")
    public String CrPCChapterDenotion;

    @Column(name = "CrPCChapterDescription")
    public String CrPCChapterDescription;

    @Column(name = "CrPCSectionRange")
    public String CrPCSectionRange;

    @Column(name = "CPCChapterDenotion")
    public String CPCChapterDenotion;

    @Column(name = "CPCChapterDescription")
    public String CPCChapterDescription;

    @Column(name = "CPCSectionRange")
    public String CPCSectionRange;

    @Column(name = "EvidenceChapterDenotion")
    public String EvidenceChapterDenotion;

    @Column(name = "EvidenceChapterDescription")
    public String EvidenceChapterDescription;

    @Column(name = "EvidenceSectionRange")
    public String EvidenceSectionRange;

    @Column(name = "ConstiChapterDenotion")
    public String ConstiChapterDenotion;

    @Column(name = "ConstiChapterDescription")
    public String ConstiChapterDescription;

    @Column(name = "ConstiSectionRange")
    public String ConstiSectionRange;

    public Chapters() {
        // Notice how super() has been called to perform default initialization
        // of our Model subclass
        super();
    }

//    public Chapters(String ChapterDenotion, String ChapterDescription, int ChapterNumber,String SectionRange) {
//        super();
//        this.ChapterDenotion = ChapterDenotion;
//        this.ChapterDescription = ChapterDescription;
//        this.ChapterNumber = ChapterNumber;
//        this.SectionStart = SectionStart;
//
//    }

    public static List<Chapters> getChapterDetails(int numberOfChapters){
        return new Select()
                .from(Chapters.class)
                .where("Id between 1 and ?", numberOfChapters)
                .execute();
    }
}