package com.example.iconfinder.repo;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.iconfinder.helpers.Constants;
import com.example.iconfinder.models.IconModel;
import com.example.iconfinder.models.IconSetModel;
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

public class IconSetRepo {

    private final Call<ResponseBody> iconSetCall;

    public IconSetRepo(Integer after) {
        //---( Setup 0 for non premium )---//
        iconSetCall = RetrofitClient.getInstance().getApi().allIconSet(Constants.TOKEN,16,0,after);
    }

    public MutableLiveData<List<IconSetModel>> getIconSets() {
        final MutableLiveData<List<IconSetModel>> iconSets = new MutableLiveData<>();

        iconSetCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    JSONObject mainJsonObject = null;
                    List<IconSetModel> iconSetModels = new ArrayList<>();

                    try {
                        mainJsonObject = new JSONObject(response.body().string());
                        //---( Extracting IconSets Array )---//
                        JSONArray iconSetsArray = mainJsonObject.optJSONArray("iconsets");
                        for (int i=0;i<iconSetsArray.length();i++){
                            IconSetModel iconSetModel = new IconSetModel();
                            JSONObject singleSet = iconSetsArray.optJSONObject(i);
                            iconSetModel.setId(singleSet.optInt("iconset_id"));
                            iconSetModel.setName(singleSet.optString("name"));

                            //---( Fetch it's icons )---//
                            Log.d("HERE IN LOOP == ","");
                            List<IconModel> icons = getIconFromIconSet(iconSetModel.getId());
                            iconSetModel.setIcons(icons);

                            //---( adding to main list )---//
                            iconSetModels.add(iconSetModel);
                        }

                        //---( Updating mutable Live data )---//
                        iconSets.postValue(iconSetModels);

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        Log.i("ERROR"," === "+response.errorBody().string());
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

        return iconSets;
    }

    private List<IconModel> getIconFromIconSet(int id) {
        Log.i("HERE IN ICONS == ","");
        Call<ResponseBody> iconsCall = RetrofitClient.getInstance().getApi().mainIconsApi(Constants.TOKEN,id,100);

        final List<IconModel> mainIcons = new ArrayList<>();

        iconsCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    JSONObject mainJsonObject = null;
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
                            JSONObject size256 = sizeArray.optJSONObject(7);
                            JSONArray formats = size256.optJSONArray("formats");
                            assert formats != null;
                            JSONObject finalSizes = formats.optJSONObject(0);
                            tempModel.setPreviewUrl(finalSizes.optString("preview_url"));
                            tempModel.setDownloadUrl(finalSizes.optString("download_url"));

                            //---( Name from first Tag )---//
                            JSONArray tags = iconObject.optJSONArray("tags");
                            assert tags != null;
                            tempModel.setName(tags.optString(0));

                            //---( Adding final Model to the List )---//
                            mainIcons.add(tempModel);
                        }
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
