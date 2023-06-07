package com.example.iconfinder.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.iconfinder.helpers.Constants;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchIconRepo {

    private final Call<ResponseBody> searchCall;

    public SearchIconRepo(String query,Integer offset) {
        searchCall = RetrofitClient.getInstance().getApi().searchIcon(Constants.TOKEN,query,40,0,offset);
    }

    public MutableLiveData<List<IconModel>> getMainIcons() {
        final MutableLiveData<List<IconModel>> mainIcons = new MutableLiveData<>();

        searchCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject mainJsonObject = null;
                    List<IconModel> tempList = new ArrayList<>();
                    //---( Deserialization of JSON )---//
                    try {
                        mainJsonObject = new JSONObject(response.body().string());
                        JSONArray allIcons = mainJsonObject.optJSONArray("icons");
                        for (int i=0;i<allIcons.length();i++){
                            JSONObject iconObject = allIcons.optJSONObject(i);
                            IconModel tempModel = new IconModel();
                            //---( Is Premium and IconID )---//
                            tempModel.setPremium(iconObject.optBoolean("is_premium"));
                            tempModel.setId(iconObject.optInt("icon_id"));

                            //---( Preview Url + Download Url )---//
                            JSONArray sizeArray = iconObject.optJSONArray("raster_sizes");
                            assert sizeArray != null;

                            for (int j=sizeArray.length();j>0;j--){
                                int size = sizeArray.optJSONObject(j-1).optInt("size");
                                if (size < 300) {
                                    JSONObject size256 = sizeArray.optJSONObject(j-1);
                                    JSONArray formats = size256.optJSONArray("formats");
                                    assert formats != null;
                                    JSONObject finalSizes = formats.optJSONObject(0);
                                    tempModel.setPreviewUrl(finalSizes.optString("preview_url"));
                                    tempModel.setDownloadUrl(finalSizes.optString("download_url"));
                                    break;
                                }
                            }

                            //---( Name from first Tag )---//
                            JSONArray tags = iconObject.optJSONArray("tags");
                            assert tags != null;
                            tempModel.setName(tags.optString(0));

                            //---( Adding final Model to the List )---//
                            tempList.add(tempModel);
                        }

                        //---( Updating mutable Live data )---//
                        mainIcons.postValue(tempList);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Log.d("ERROR"," === "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });

        return mainIcons;
    }
}
