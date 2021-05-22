package com.example.mobilesw.info;

// 사용자에 대한 정보 : 이름(필수), 프로필 경로
public class MemberInfo {
    private String name;
    private String profilePath;

    public MemberInfo(){}

    public MemberInfo(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getProfilePath(){
        return this.profilePath;
    }

    public void setProfilePath(String profilePath){
        this.profilePath = profilePath;
    }
}
