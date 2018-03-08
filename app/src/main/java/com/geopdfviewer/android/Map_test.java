package com.geopdfviewer.android;

import android.net.Uri;

/**
 * Created by 54286 on 2018/3/6.
 */

public class Map_test {
    private String m_name;
    private int m_num;
    private String m_GPTS;
    private String m_BBox;
    private String m_WKT;
    private Uri m_uri;
    private String m_imguri;

    public Map_test(int num, String name, String WKT, Uri uri, String GPTS, String BBox, String imguri) {
        m_BBox = BBox;
        m_num = num;
        m_name = name;
        m_WKT = WKT;
        m_uri = uri;
        m_GPTS = GPTS;
        m_imguri = imguri;
    }

    public String getM_imguri() {
        return m_imguri;
    }

    public void setM_imguri(String m_imguri) {
        this.m_imguri = m_imguri;
    }

    public Map_test(String name) {
        m_name = name;
    }
    public Map_test(String name, Uri uri) {
        m_name = name;
        m_uri = uri;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public int getM_num() {
        return m_num;
    }

    public void setM_num(int m_num) {
        this.m_num = m_num;
    }

    public String getM_GPTS() {
        return m_GPTS;
    }

    public void setM_GPTS(String m_GPTS) {
        this.m_GPTS = m_GPTS;
    }

    public String getM_BBox() {
        return m_BBox;
    }

    public void setM_BBox(String m_BBox) {
        this.m_BBox = m_BBox;
    }

    public String getM_WKT() {
        return m_WKT;
    }

    public void setM_WKT(String m_WKT) {
        this.m_WKT = m_WKT;
    }

    public Uri getM_uri() {
        return m_uri;
    }

    public void setM_uri(Uri m_uri) {
        this.m_uri = m_uri;
    }
}
