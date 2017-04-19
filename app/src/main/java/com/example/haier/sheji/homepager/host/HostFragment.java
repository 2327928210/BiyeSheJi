package com.example.haier.sheji.homepager.host;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.haier.sheji.Constants;
import com.example.haier.sheji.R;
import com.example.haier.sheji.homepager.host.Adapter.HotFragmentAdapter;
import com.example.haier.sheji.homepager.host.Hot_Fragment_Second.Second_WebActivity;
import com.example.haier.sheji.homepager.host.bean.HotFragmentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HostFragment extends Fragment implements View.OnClickListener{


    private RecyclerView recyclerView;
    private List<HotFragmentBean>data;//存放适配器的数据

    private HotFragmentBean hotFragmentBean;

    private HotFragmentAdapter hotFragmentAdapter;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private  int mCurrentPage = 1;
    private  int Page;
    private String url1;
    //------------6666-------------------------------------------------------------
    private boolean isLoading = false;  //false代表当前没有在加载
    //------------6666-------------------------------------------------------------
    private  String nextsign;
    private String nexttime;
    private View circularProgressBar;
    private View view;
    // private View view;


    public HostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 /*
 //***************************显示dialoag*****************************
       //注意这个是可以正常解封的，解封之后是对话框的形式，不过有点丑，弃只不用
        builder = new AlertDialog.Builder(getActivity(),R.style.draw_dialog).create();
        builder.setCancelable(false);//是否可撤销
          Window window = builder.getWindow();
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.loading,null);
        WindowManager.LayoutParams wm = window.getAttributes();
       wm.width = 350; // 设置对话框的宽
        wm.height = 350; // 设置对话框的高
        wm.alpha = 1.0f;   // 对话框背景透明度
        wm.dimAmount = 0.6f; // 遮罩层亮度
        window.setAttributes(wm);
 //*****************************和上面的一样******************************
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(params);
**************************************************************************


        ImageView imageView = (ImageView) view1.findViewById(R.id.loading);
        RotateAnimation animation = new RotateAnimation(0,359, Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);//RELATIVE_TO_SELF相对自己
        animation.setFillAfter(true);
        animation.setDuration(2000);//持续时间
        animation.setRepeatCount(Animation.INFINITE);//设置为无限循环RepeatCount重复次数，RepeatCount无限
        animation.setRepeatMode(Animation.RESTART);//RESTART重新开始
        animation.setInterpolator(getContext(),android.R.anim.linear_interpolator);//Interpolator插值器，linear_interpolator线性插值器
        imageView.setAnimation(animation);
        builder.setView(view1);
        builder.show();
        //***************************显示dialoag*****************************
        */

        data = new ArrayList<>();
        VollsyRequest(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_hot,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.HotFragment_RecyclerView);
        //下拉加载更多
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_pull);
        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager);
        hotFragmentAdapter = new HotFragmentAdapter(getContext(),data);
        hotFragmentAdapter.setClickListener(this);

        // hotFragmentAdapter.setLongClickLinstener(this);长点击的
        recyclerView.setAdapter(hotFragmentAdapter);
//******************************progressbar****************
        /*注意，我尝试路好几种方法，onCreate里面只创建一次progressbar已经无解，突然想起判断数据的方法，
        *frangment滑到第三页回到第一页会重走 onCreateView方法，这样会导致progressbary又被创建并显示，
        * 而适配器不会再刷新，progressbar会一直显示,为了解决这个问题，就需要判断当前fragment的集合里有没有数据
        * 有数据就把progressbar隐藏
        */
        circularProgressBar=  view.findViewById(R.id.progressbar);
        if (circularProgressBar != null) {
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        if (data.size()!=0){
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(View.GONE);
            }
        }
        //******************************progressbar****************
        setListener();//下拉刷新监听
        return view;
    }


    //+++++++++++++++++++++++++++++跳转第二页的点击事件++++++++++++++++++
    @Override
    public void onClick(View v) {
        //v代表的是哪个控件被点了就代表了哪个控件
//        String tag = (String) v.getTag();
//        if (tag!=null){
//            Log.e("TAG", "onClick:第二中方法 " +tag);
//        }
        Integer tag = (Integer) v.getTag();
        if (tag!=null) {
            Log.e("TAG", "onClick:第二中方法 " + tag);
            Intent intent = new Intent(getActivity(),Second_WebActivity.class);
            intent.putExtra("query_string",data.get(tag).getQuery_string());
            Log.e("TAG", "onClick:携带的跳转数据 "+data.get(tag).getQuery_string());
            startActivity(intent);

        }

    }

    //+++++++++++++++++++++++++++++跳转第二页的点击事件++++++++++++++++++
/*//长点击的
    @Override
    public boolean onLongClick(View v) {
      Integer   tag = (Integer) v.getTag();
        if (tag!=null) {
            Log.e("TAG", "onClick:第二中方法长按点击" + tag);
        }
        return true;//fase为你点击了会附带短点击的，true长点击就是长点击，
    }
    */
    private void setListener() {
//*******************设置swipeRefreshLayout刷新颜色*******************
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.green,R.color.red,R.color.yellow);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                VollsyRequest(1);

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //------------6666-------------------------------------------------------------
                if (newState == recyclerView.SCROLL_STATE_IDLE&&manager.findLastVisibleItemPosition()==data.size()-1&&!isLoading){
                    Log.e("tag", "onScrollStateChanged: "+mCurrentPage);
                    VollsyRequest(mCurrentPage);
                    mCurrentPage++;
                    //------------6666-------------------------------------------------------------
                }
            }
        });
    }
    //public void  LoadData(){
//}
    public void VollsyRequest( int page) {
        if (page == 1) {
            data.clear();
            StringRequest stringRequest = new StringRequest(StringRequest.Method.GET,
                    Constants.Home_Hot_Fast+page,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                        HotFragmentBean hotFragmentBean = new HotFragmentBean();
//                       Gson gson = new Gson();
//                        HotFragmentBean hotFragmentBean = gson.fromJson(response, HotFragmentBean.class);
//                          hotFragmentBean.;

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsondata = jsonObject.getJSONObject("data");
                                //-----下一页数据-----
                                nexttime = jsondata.getString("nexttime");
                                nextsign = jsondata.getString("nextsign");
//                                nextPageBean = new NextPageBean();
//                                nextPageBean.setNexttime(nexttime);
//                                nextPageBean.setNextsign(nextsign);
//                                nextpage.add(nextPageBean);
                                url1 = Constants.Home_Hot_Fast1+nexttime+Constants.Home_Hot_Fast2+nextsign;
                                //-----下一页数据-----
                                JSONArray list = jsondata.getJSONArray("list");

                                for (int i = 0; i < list.length(); i++) {
                                    hotFragmentBean = new HotFragmentBean();
                                    JSONObject listItem = list.getJSONObject(i);
                                    String Id = listItem.getString("Id");
                                    String title = listItem.getString("title");
                                    String img_src = listItem.getString("img_src");
                                    String cate_title = listItem.getString("cate_title");
                                    String query_string = listItem.getString("query_string");
                                    hotFragmentBean.setId(Id);
                                    hotFragmentBean.setTitle(title);
                                    hotFragmentBean.setImg_src(img_src);
                                    hotFragmentBean.setCate_title(cate_title);
                                    hotFragmentBean.setQuery_string(query_string);
                                    JSONObject display0bject = listItem.getJSONObject("display");
                                    String value = display0bject.getString("value");
                                    hotFragmentBean.setValue(value);
                                    data.add(hotFragmentBean);
                                }

                                hotFragmentAdapter.notifyDataSetChanged();
                                //  builder.dismiss();
                                if (circularProgressBar != null) {
                                    circularProgressBar.setVisibility(View.GONE);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e("TAG", "onResponse: " + data);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            mCurrentPage = page;
            mCurrentPage++;
            stringRequest.setTag("rquest");

            Volley.newRequestQueue(getContext()).add(stringRequest);
        }else{



//---------------------又重新加载一遍网络请求了，不过要改网址----------------------------------------------
            StringRequest stringRequest = new StringRequest(StringRequest.Method.GET,
                    url1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                        HotFragmentBean hotFragmentBean = new HotFragmentBean();
//                       Gson gson = new Gson();
//                        HotFragmentBean hotFragmentBean = gson.fromJson(response, HotFragmentBean.class);
//                          hotFragmentBean.;

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsondata = jsonObject.getJSONObject("data");

//                                nextPageBean = new NextPageBean();
//                                nextPageBean.setNexttime(nexttime);
//                                nextPageBean.setNextsign(nextsign);
//                                nextpage.add(nextPageBean);
                                //-----下一页数据-----
                                JSONArray list = jsondata.getJSONArray("list");

                                for (int i = 0; i < list.length(); i++) {
                                    hotFragmentBean = new HotFragmentBean();
                                    JSONObject listItem = list.getJSONObject(i);
                                    String Id = listItem.getString("Id");
                                    String title = listItem.getString("title");
                                    String img_src = listItem.getString("img_src");
                                    String cate_title = listItem.getString("cate_title");
                                    String query_string = listItem.getString("query_string");
                                    hotFragmentBean.setId(Id);
                                    hotFragmentBean.setTitle(title);
                                    hotFragmentBean.setImg_src(img_src);
                                    hotFragmentBean.setCate_title(cate_title);
                                    hotFragmentBean.setQuery_string(query_string);
                                    JSONObject display0bject = listItem.getJSONObject("display");
                                    String value = display0bject.getString("value");
                                    hotFragmentBean.setValue(value);
                                    data.add(hotFragmentBean);
                                }
                                hotFragmentAdapter.notifyDataSetChanged();
                                //  builder.dismiss();
                                if (circularProgressBar != null) {
                                    circularProgressBar.setVisibility(View.GONE);
                                }

                                //-----下一页数据-----
                                nexttime = jsondata.getString("nexttime");
                                nextsign = jsondata.getString("nextsign");
                                url1 = Constants.Home_Hot_Fast1+nexttime+Constants.Home_Hot_Fast2+nextsign;
                                //------------6666-------------------------------------------------------------
                                isLoading = false;
                                //------------6666-------------------------------------------------------------
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e("TAG", "onResponse: " + data);
//                            swipeRefreshLayout.setRefreshing(false);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
            //------------

            hotFragmentAdapter.notifyDataSetChanged();
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(View.GONE);
            }

            //--------------
            stringRequest.setTag("rquest");
            Volley.newRequestQueue(getContext()).add(stringRequest);

//---------------------又重新加载一遍网络请求了，不过要改网址----------------------------------------------
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Volley.newRequestQueue(getContext()).cancelAll("rquest");
    }

}