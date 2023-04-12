package reflection;

import lombok.Data;

public class ProgramMonkey extends Person implements ICompany {
    String mLanguage = "C#";
    private String mCompany = "BBK";

    public ProgramMonkey(String aName, String aSex, int aAge) {
        super(aName, aSex, aAge);
    }

    public ProgramMonkey(String language, String company, String aName, String aSex, int aAge) {
        super(aName, aSex, aAge);
        mLanguage = language;
        mCompany = company;
    }


    public String getmLanguage() {
        return mLanguage;
    }

    public void setmLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    private int getSalaryPerMonth() {
        return 121212;
    }

    @Override
    public String getCompany() {
        return mCompany;
    }
}
