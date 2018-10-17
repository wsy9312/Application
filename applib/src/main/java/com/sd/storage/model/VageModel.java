package com.sd.storage.model;

import com.sd.storage.actions.CommentModel;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-09-05.
 */

public class VageModel {


 /*"
                    "vegeid":22,
                    "dayid":1,
                    "typeid":2,
                    "vegename":"包子",
                    "vegedesc":"好吃",
                    "vegeimg":"ggg",
                    "vegestate":1,
                    "vegegive":0,
                    "vegecomment":0,
                    "vegevotestatus":null,
                    "vegevote":1,
                    "heatid":0,
                    "coms":null
    */

    public VageModel(String vegeid, String dayid, String typeid, String vegename, String vegedesc, String vegeimg, int vegestate, String vegegive, String vegecomment, String vegevotestatus, String vegevote, String heatid) {
        this.vegeid = vegeid;
        this.dayid = dayid;
        this.typeid = typeid;
        this.vegename = vegename;
        this.vegedesc = vegedesc;
        this.vegeimg = vegeimg;
        this.vegestate = vegestate;
        this.vegegive = vegegive;
        this.vegecomment = vegecomment;
        this.vegevotestatus = vegevotestatus;
        this.vegevote = vegevote;
        this.heatid = heatid;
    }



    public String foodid;
    public String vegeid;
    public String dayid;
    public String typeid;
    public String vegename;

    public String vegedesc;
    public String vegeimg;
    public int vegestate;
    public String vegegive;



    public String vegecomment;
    public String vegevotestatus;
    public String vegevote;
    public String heatid;
    public ArrayList<CommentModel> coms;

    public VageModel(String vegename) {
        this.vegename = vegename;
    }

    public void setVegedesc(String vegedesc) {
        this.vegedesc = vegedesc;
    }

    public void setVegestate(int vegestate) {
        this.vegestate = vegestate;
    }

    public void setVegegive(String vegegive) {
        this.vegegive = vegegive;
    }
}
