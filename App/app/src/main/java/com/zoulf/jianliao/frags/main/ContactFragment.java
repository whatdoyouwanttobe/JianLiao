package com.zoulf.jianliao.frags.main;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.zoulf.common.app.MyFragment;
import com.zoulf.common.widget.EmptyView;
import com.zoulf.common.widget.PortraitView;
import com.zoulf.common.widget.recycler.RecyclerAdapter;
import com.zoulf.factory.model.db.User;
import com.zoulf.jianliao.R;

/**
 * @author Zoulf.
 */
public class ContactFragment extends MyFragment {

  @BindView(R.id.empty)
  EmptyView mEmptyView;

  @BindView(R.id.recycler)
  RecyclerView mRecycler;

  // 适配器，User，可以直接从数据库查询数据
  private RecyclerAdapter<User> mAdapter;

  public ContactFragment() {
    // Required empty public constructor
  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_contact;
  }

  @Override
  protected void initWidget(View root) {
    super.initWidget(root);
    // 初始化Recycler
    mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecycler.setAdapter(mAdapter = new RecyclerAdapter<User>() {
      @Override
      protected int getItemViewType(int position, User userCard) {
        // 返回cell的布局id
        return R.layout.cell_contact_list;
      }

      @Override
      protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
        return new ContactFragment.MyViewHolder(root);
      }
    });

    mEmptyView.bind(mRecycler);
    setPlaceHolderView(mEmptyView);
  }

  class MyViewHolder extends RecyclerAdapter.ViewHolder<User> {

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.txt_name)
    TextView mName;

    @BindView(R.id.txt_desc)
    TextView mDesc;

    public MyViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onBind(User user) {
      mPortraitView.setup(Glide.with(ContactFragment.this), user);
      mName.setText(user.getName());
      mDesc.setText(user.getDesc());
    }
  }
}
