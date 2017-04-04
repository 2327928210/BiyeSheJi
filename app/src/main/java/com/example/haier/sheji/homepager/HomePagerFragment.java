package com.example.haier.sheji.homepager;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haier.sheji.R;
import com.example.haier.sheji.homepager.ShengHuo.ShengHuoFragment;
import com.example.haier.sheji.homepager.baoxiao.BaoXiaoFragment;
import com.example.haier.sheji.homepager.host.HostFragment;
import com.example.haier.sheji.homepager.hudong.HuDongFragment;
import com.example.haier.sheji.homepager.mengchong.MengChongFragment;
import com.example.haier.sheji.homepager.qiqu.QiQuFragment;
import com.example.haier.sheji.homepager.yule.YuLeFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePagerFragment extends Fragment {


    public HomePagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_pager,container,false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.HomeFragment_ViewPager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.HomeFragment_TableLayout);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HostFragment());//热门
        fragments.add(new HuDongFragment());//互动
        fragments.add(new QiQuFragment());//奇趣
        fragments.add(new YuLeFragment());//娱乐
        fragments.add(new MengChongFragment());//萌宠
        fragments.add(new BaoXiaoFragment());//爆笑
        fragments.add(new ShengHuoFragment());//生活
        List<String> titles = new ArrayList<>();
        titles.add("热门");
        titles.add("互动");
        titles.add("奇趣");
        titles.add("娱乐");
        titles.add("萌宠");
        titles.add("爆笑");
        titles.add("生活");
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getChildFragmentManager(),fragments,titles);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

}
