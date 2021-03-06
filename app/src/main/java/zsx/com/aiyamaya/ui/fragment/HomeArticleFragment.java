package zsx.com.aiyamaya.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import zsx.com.aiyamaya.R;
import zsx.com.aiyamaya.adapter.HomeArticleAdapter;
import zsx.com.aiyamaya.api.OkHttpHelp;
import zsx.com.aiyamaya.api.OkHttpNewsHelp;
import zsx.com.aiyamaya.item.ArticleItem;
import zsx.com.aiyamaya.item.NewsDataItem;
import zsx.com.aiyamaya.item.ResultItem;
import zsx.com.aiyamaya.item.TouTiaoItem;
import zsx.com.aiyamaya.item.TouTiaoNewsItem;
import zsx.com.aiyamaya.listener.ResponseListener;
import zsx.com.aiyamaya.util.Constant;
import zsx.com.aiyamaya.util.MyUtil;
import zsx.com.aiyamaya.util.ProgressDialogUtil;

import static zsx.com.aiyamaya.util.MyUtil.T;

public class HomeArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ArticleFragment";

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int refresh = 0x100;

    private List<ArticleItem> articleList = new ArrayList<>();

    private List<NewsDataItem> newsList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case refresh:

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fa_rv_recycleview);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fa_sr_swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
    }

    @Override
    public void onRefresh() {
        getData();
//        handler.sendEmptyMessageDelayed(refresh, 3000);
    }

    private void getData() {
        articleList.clear();
        newsList.clear();
        ProgressDialogUtil.showProgressDialog(getActivity(), true);
        Map<String, String> map = new HashMap<>();
//        map.put("random","7");
        OkHttpNewsHelp<NewsDataItem> httpHelp = OkHttpNewsHelp.getInstance();
        httpHelp.httpRequest("", "", map, new ResponseListener<NewsDataItem>() {
            @Override
            public void onSuccess(NewsDataItem object) {
                newsList.add(object);
                ProgressDialogUtil.dismissProgressdialog();
                getDatas();
//                if(object.getResult().equals("success")){
//                    try {
//                        JSONArray jsonArray=new JSONArray(object.getData());
//                        for(int i=0;i<jsonArray.length();i++){
//                            ArticleItem articleItem=  new Gson().fromJson(jsonArray.get(i).toString(), ArticleItem.class);
//                            articleList.add(articleItem);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        MyUtil.MyLogE(TAG,e.toString());
//                    }
//                    mRecyclerView.setAdapter(new HomeArticleAdapter(getActivity(),articleList));
//                    if(getActivity()!=null && swipeRefreshLayout.isRefreshing()){
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }
            }

            @Override
            public void onFailed(String message) {
                ProgressDialogUtil.dismissProgressdialog();
            }

            @Override
            public Class<NewsDataItem> getEntityClass() {
                return NewsDataItem.class;
            }
        });
    }

    private void getDatas() {
        mRecyclerView.setAdapter(new HomeArticleAdapter(getActivity(), newsList));
        if (getActivity() != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
