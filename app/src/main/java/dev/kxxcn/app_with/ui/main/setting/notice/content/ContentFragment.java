package dev.kxxcn.app_with.ui.main.setting.notice.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.kxxcn.app_with.R;
import dev.kxxcn.app_with.data.model.notice.Notice;

/**
 * Created by kxxcn on 2018-12-26.
 */
public class ContentFragment extends Fragment {

    private static final String KEY_NOTICE = "KEY_NOTICE";

    @BindView(R.id.tv_content)
    TextView tv_content;

    private int mPosition;

    public static ContentFragment newInstance(ArrayList<Notice> noticeList) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_NOTICE, noticeList);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Bundle args = getArguments();
            if (args != null) {
                List<Notice> noticeList = args.getParcelableArrayList(KEY_NOTICE);
                if (noticeList != null) {
                    tv_content.setText(noticeList.get(mPosition).getContent());
                }
            }
        }
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

}
