package com.zoulf.jianliao.frags.search;



import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.zoulf.common.app.PresenterFragment;
import com.zoulf.common.widget.EmptyView;
import com.zoulf.common.widget.PortraitView;
import com.zoulf.common.widget.recycler.RecyclerAdapter;
import com.zoulf.factory.model.card.UserCard;
import com.zoulf.factory.presenter.search.SearchContract;
import com.zoulf.factory.presenter.search.SearchContract.Presenter;
import com.zoulf.factory.presenter.search.SearchUserPresenter;
import com.zoulf.jianliao.R;
import com.zoulf.jianliao.activities.SearchActivity.SearchFragment;
import java.util.List;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter>
    implements SearchFragment, SearchContract.UserView {

  @BindView(R.id.empty)
  EmptyView mEmptyView;

  @BindView(R.id.recycler)
  RecyclerView mRecycler;

  private RecyclerAdapter<UserCard> mAdapter;


  public SearchUserFragment() {

  }

  @Override
  protected int getContentLayoutId() {
    return R.layout.fragment_search_user;
  }

  @Override
  protected void initWidget(View root) {
    super.initWidget(root);
    // 初始化Recycler
    mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    mRecycler.setAdapter(mAdapter = new RecyclerAdapter<UserCard>() {
      @Override
      protected int getItemViewType(int position, UserCard userCard) {
        // 返回cell的布局id
        return R.layout.cell_search_list;
      }

      @Override
      protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
        return new MyViewHolder(root);
      }
    });

    mEmptyView.bind(mRecycler);
    setPlaceHolderView(mEmptyView);
  }

  @Override
  protected void initData() {
    super.initData();
    // 发起首次搜索
    search("");
  }

  @Override
  public void search(String content) {
    // Activity -> Fragment -> Presenter -> Network -> Back
    mPresenter.search(content);
  }

  @Override
  protected Presenter initPresenter() {
    // 初始化Presenter
    return new SearchUserPresenter(this);
  }

  @Override
  public void onSearchDone(List<UserCard> userCards) {
    // 数据成功的情况下返回数据
    mAdapter.replace(userCards);
    // 如果有数据，则是OK，没有数据则显示空布局
    mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
  }

  /**
   * 每一个Cell的布局操作
   */
  class MyViewHolder extends RecyclerAdapter.ViewHolder<UserCard> {

    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    @BindView(R.id.txt_name)
    TextView mName;

    @BindView(R.id.im_follow)
    ImageView mFollow;

    public MyViewHolder(View itemView) {
      super(itemView);
    }

    @Override
    protected void onBind(UserCard userCard) {
      Glide.with(SearchUserFragment.this)
          .load(userCard.getPortrait())
          .centerCrop()
          .into(mPortraitView);

      mName.setText(userCard.getName());
      mFollow.setEnabled(!userCard.isFollow());
    }

    @OnClick(R.id.im_follow)
    void onFollowClick() {
      // 发起关注

    }
  }
}
