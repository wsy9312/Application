package com.sd.storage.api;


import com.sd.storage.actions.CommentModel;
import com.sd.storage.dlib.model.DataContainer;
import com.sd.storage.model.VageModel;
import com.sd.storage.model.VegeTitle;
import com.sd.storage.model.VoteTimeModel;
import com.sd.storage.model.WeekMeunModel;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by  on 2016/9/26.
 */
public interface ApiService {


    @GET("/Canteen/vege/vegelist.do")
    Observable<DataContainer<ArrayList<WeekMeunModel>>> WeekMeun(@QueryMap Map<String, String> params);


    @GET("/Canteen/coment/addComent.do")
    Observable<DataContainer<Object>> AddComment(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/selectVege.do")
    Observable<DataContainer<VegeTitle>> getVageDetails(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/coment.do")
    Observable<DataContainer<ArrayList<CommentModel>>> getCommentList(@QueryMap Map<String, String> params);

    @GET("/Canteen/vege/stats.do")
    Observable<DataContainer<Object>> setGive(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/vegename.do")
    Observable<DataContainer<ArrayList<VageModel>>> getSearchList(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/VegegiveOrder.do")
    Observable<DataContainer<ArrayList<VageModel>>> getVegegiveOrder(@QueryMap Map<String, String> params);

    @GET("/Canteen/vege/allFoodstore.do")
    Observable<DataContainer<ArrayList<VageModel>>> getAllMeunList(@QueryMap Map<String, String> params);

    @GET("/Canteen/vegeorder/addVegeOrder.do")
    Observable<DataContainer<Object>> addVegeOrder(@QueryMap Map<String, String> params);


    @GET("/Canteen/vegeorder/deleteVegeOrder.do")
    Observable<DataContainer<Object>> deleteVegeOrder(@QueryMap Map<String, String> params);

    @GET("/Canteen/vegeOrderLast/addVegeOrderLast.do")
    Observable<DataContainer<Object>> addVegeOrderLast(@QueryMap Map<String, String> params);


    @GET("/Canteen/vegeorder/selectVegeOrder.do")
    Observable<DataContainer<ArrayList<VageModel>>> getSelectVegeList(@QueryMap Map<String, String> params);


    @GET("/Canteen/vegeOrderLast/selectAllVegeOrderLast.do")
    Observable<DataContainer<ArrayList<VageModel>>> getVoteListList(@QueryMap Map<String, String> params);



    @GET("/Canteen/vegeOrderLast/vote.do")
    Observable<DataContainer<Object>> setVote(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/addFoodstore.do")
    Observable<DataContainer<Object>> addNewVege(@QueryMap Map<String, String> params);



    @GET("/Canteen/vege/deleteVege.do")
    Observable<DataContainer<Object>> deleteVege(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/foodstoreName.do")
    Observable<DataContainer<ArrayList<VageModel>>> getSearchStoreList(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/deleteFoodstore.do")
    Observable<DataContainer<Object>> deleteFoodstore(@QueryMap Map<String, String> params);



    @Multipart
    @POST("Canteen/vege/addFoodstore.do")
    Observable<DataContainer<Object>> uploadImage(@QueryMap Map<String, String> params, @Part MultipartBody.Part file);


    @GET("/Canteen/vege/addVege.do")
    Observable<DataContainer<Object>> addVegeWeek(@QueryMap Map<String, String> params);



    @GET("/Canteen/VoteTime/addVoteTime.do")
    Observable<DataContainer<Object>> setTime(@QueryMap Map<String, String> params);


    @GET("/Canteen/VoteTime/selectAllVoteTime.do")
    Observable<DataContainer<ArrayList<VoteTimeModel>>> selectAllVoteTime(@QueryMap Map<String, String> params);




    @GET("/Canteen/VoteTime/VoteTime.do")
    Observable<DataContainer<Object>> voteTime(@QueryMap Map<String, String> params);


    @GET("/Canteen/vege/voteSize.do")
    Observable<DataContainer<Object>> voteSize(@QueryMap Map<String, String> params);

}
