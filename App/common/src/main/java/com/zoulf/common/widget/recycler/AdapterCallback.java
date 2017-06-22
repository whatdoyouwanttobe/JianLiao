package com.zoulf.common.widget.recycler;

/**
 * @author Zoulf
 */

public interface AdapterCallback<Data> {

  void update(Data data, RecyclerAdapter.ViewHolder<Data> holder);

}
