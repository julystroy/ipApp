package com.cartoon.data;


import android.os.Parcel;
import android.os.Parcelable;

public class CardItem implements Parcelable{
    /**
     * des :
     * question : 人物问题3
     * book : 玄界之门
     * option3 : 人物正确答案3
     * option4 : 人物错误2
     * option1 : 人物错误3
     * id : 6
     * option2 : 人物错误1
     * position : 1
     * type : 人物型
     */

    private String des;
    private String question;
    private String book;
    private String option3;
    private String option4;
    private String option1;
    private int    id;
    private String option2;
    private int    position;
    private String type;

    public CardItem(){}
    protected CardItem(Parcel in) {
        des = in.readString();
        question = in.readString();
        book = in.readString();
        option3 = in.readString();
        option4 = in.readString();
        option1 = in.readString();
        id = in.readInt();
        option2 = in.readString();
        position = in.readInt();
        type = in.readString();
    }

    @Override
    public String toString() {
        return "CardItem{" +
                "des='" + des + '\'' +
                ", question='" + question + '\'' +
                ", book='" + book + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", option1='" + option1 + '\'' +
                ", id=" + id +
                ", option2='" + option2 + '\'' +
                ", position=" + position +
                ", type='" + type + '\'' +
                '}';
    }

    public static final Creator<CardItem> CREATOR = new Creator<CardItem>() {
        @Override
        public CardItem createFromParcel(Parcel in) {
            return new CardItem(in);
        }

        @Override
        public CardItem[] newArray(int size) {
            return new CardItem[size];
        }
    };

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CardItem(String question, String option1, String option2, String option3, String option4, int position,int id) {
        this.question = question;
        this.option3 = option3;
        this.option4 = option4;
        this.option1 = option1;
        this.id = id;
        this.option2 = option2;
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(des);
        dest.writeString(question);
        dest.writeString(book);
        dest.writeString(option3);
        dest.writeString(option4);
        dest.writeString(option1);
        dest.writeInt(id);
        dest.writeString(option2);
        dest.writeInt(position);
        dest.writeString(type);
    }

    /* private int mTextResource;
    private int mTitleResource;
    private int aa;
    private int bb;
    private int cc;
    private int dd;

    public CardItem(int title, int text,int a,int b, int c, int d) {
        mTitleResource = title;
        mTextResource = text;
        aa = a;
        bb = b;
        cc = c;
        dd = d;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }

    public int getA(){return aa;}
    public int getB(){return bb;}
    public int getC(){return cc;}
    public int getD(){return dd;}*/


}
