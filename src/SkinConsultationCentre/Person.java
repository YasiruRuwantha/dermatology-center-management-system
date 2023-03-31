package SkinConsultationCentre;

class Person {
    private String fName;
    private String surName;
    private String dob;
    private String contactNo;

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public Person(String fName, String surName, String dob, String contactNo) {
        this.fName = fName;
        this.surName = surName;
        this.dob = dob;
        this.contactNo = contactNo;
    }

}