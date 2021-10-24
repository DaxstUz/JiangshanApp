package com.jiangshan.knowledge.activity.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.http.EasyHttp;
import com.hjq.http.listener.HttpCallback;
import com.jiangshan.knowledge.R;
import com.jiangshan.knowledge.activity.BaseActivity;
import com.jiangshan.knowledge.activity.home.adapter.ChapterAdapter;
import com.jiangshan.knowledge.http.api.GetChapterApi;
import com.jiangshan.knowledge.http.entity.Chapter;
import com.jiangshan.knowledge.http.entity.Course;
import com.jiangshan.knowledge.http.entity.Subject;
import com.jiangshan.knowledge.http.model.HttpListDataAll;
import com.jiangshan.knowledge.uitl.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * auth s_yz  2021/10/20
 */
public class ChapterListActivity extends BaseActivity {

    private RecyclerView rvChapter;
    private ChapterAdapter chapterAdapter;
    private List<Chapter> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        setTitle("江山老师题库");
        setBackViewVisiable();

        initView();

        getChapterData();
    }

    private void initView() {
        rvChapter = findViewById(R.id.rv_chapter);
        chapterAdapter = new ChapterAdapter(R.layout.item_chapter, datas);
        rvChapter.setLayoutManager(new LinearLayoutManager(this));
        rvChapter.setAdapter(chapterAdapter);
    }

    private void getChapterData() {
        Subject subject = LocalDataUtils.getSubject(this);
        Course course = LocalDataUtils.getCourse(this);
        EasyHttp.get(this)
                .api(new GetChapterApi().setSubjectCode(subject.getSubjectCode()).setCourseCode(course.getCourseCode()))
                .request(new HttpCallback<HttpListDataAll<Chapter>>(this) {
                    @Override
                    public void onSucceed(HttpListDataAll<Chapter> result) {
                        datas.addAll(result.getData());
                        chapterAdapter.notifyDataSetChanged();
                    }
                });
    }
}
