package com.tmkoo.searchapi.vo;

public class TradeMarkCategory {
    private Integer id;

    private String name;

    private String enName;

    private Integer no;

    private String tmGroup;

    private Integer tmType;

    private String regNumber;

    private Integer tmId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getTmGroup() {
        return tmGroup;
    }

    public void setTmGroup(String tmGroup) {
        this.tmGroup = tmGroup == null ? null : tmGroup.trim();
    }

    public Integer getTmType() {
        return tmType;
    }

    public void setTmType(Integer tmType) {
        this.tmType = tmType;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber == null ? null : regNumber.trim();
    }

    public Integer getTmId() {
        return tmId;
    }

    public void setTmId(Integer tmId) {
        this.tmId = tmId ;
    }
}