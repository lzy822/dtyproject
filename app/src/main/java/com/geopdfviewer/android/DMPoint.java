package com.geopdfviewer.android;

import org.litepal.crud.LitePalSupport;

public class DMPoint extends LitePalSupport{
    private long xh;
    private String qydm;
    private String lbdm;
    private String bzmc;
    private String cym;
    private String jc;
    private String bm;
    private String dfyz;
    private String zt;
    private String dmll;
    private String dmhy;
    private String lsyg;
    private String dlstms;
    private String zlly;
    private String imgpath;
    private String tapepath;
    private String time;
    private float lat;
    private float lng;

    public String getDiMingId() {
        return DiMingId;
    }

    public void setDiMingId(String diMingId) {
        DiMingId = diMingId;
    }

    public String getMapId() {
        return MapId;
    }

    public void setMapId(String mapId) {
        MapId = mapId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String DiMingId;
    private String MapId;
    private int type;

    public String getQydm() {
        return qydm;
    }

    public void setQydm(String qydm) {
        this.qydm = qydm;
    }

    public long getXh() {
        return xh;
    }

    public void setXh(long xh) {
        this.xh = xh;
    }

    public String getLbdm() {
        return lbdm;
    }

    public void setLbdm(String lbdm) {
        this.lbdm = lbdm;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getBzmc() {
        return bzmc;
    }

    public void setBzmc(String bzmc) {
        this.bzmc = bzmc;
    }

    public String getCym() {
        return cym;
    }

    public void setCym(String cym) {
        this.cym = cym;
    }

    public String getJc() {
        return jc;
    }

    public void setJc(String jc) {
        this.jc = jc;
    }

    public String getBm() {
        return bm;
    }

    public void setBm(String bm) {
        this.bm = bm;
    }

    public String getDfyz() {
        return dfyz;
    }

    public void setDfyz(String dfyz) {
        this.dfyz = dfyz;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getDmll() {
        return dmll;
    }

    public void setDmll(String dmll) {
        this.dmll = dmll;
    }

    public String getDmhy() {
        return dmhy;
    }

    public void setDmhy(String dmhy) {
        this.dmhy = dmhy;
    }

    public String getLsyg() {
        return lsyg;
    }

    public void setLsyg(String lsyg) {
        this.lsyg = lsyg;
    }

    public String getDlstms() {
        return dlstms;
    }

    public void setDlstms(String dlstms) {
        this.dlstms = dlstms;
    }

    public String getZlly() {
        return zlly;
    }

    public void setZlly(String zlly) {
        this.zlly = zlly;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getTapepath() {
        return tapepath;
    }

    public void setTapepath(String tapepath) {
        this.tapepath = tapepath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
