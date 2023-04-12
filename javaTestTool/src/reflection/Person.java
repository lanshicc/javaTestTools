package reflection;

import lombok.Data;

public class Person {
    String mName;
    String mSex;
    public int mAge;

    public Person(String aName, String aSex, int aAge) {
        mName = aName;
        mSex = aSex;
        mAge = aAge;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public void setmAge(int mAge) {
        this.mAge = mAge;
    }

    private String getDescription() {
        return "黄种人";
    }
}
