package com.example.group3_fragment;

public class Student {
    private  String code;
    private String fullName;
    private String className;
    private String pointAgv;
    public  Student(String code,String fullName,String className,String pointAgv){
        this.code=code;
        this.fullName=fullName;
        this.className=className;
        this.pointAgv=pointAgv;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPointAgv() {
        return pointAgv;
    }

    public void setPointAgv(String pointAgv) {
        this.pointAgv = pointAgv;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

