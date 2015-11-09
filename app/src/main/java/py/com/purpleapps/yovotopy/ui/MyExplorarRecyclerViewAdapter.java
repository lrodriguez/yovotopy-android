package py.com.purpleapps.yovotopy.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.model.Candidato;
import py.com.purpleapps.yovotopy.model.Departamento;
import py.com.purpleapps.yovotopy.model.Distrito;
import py.com.purpleapps.yovotopy.model.Partido;
import py.com.purpleapps.yovotopy.ui.ExplorarFragment.OnListFragmentInteractionListener;
import py.com.purpleapps.yovotopy.ui.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link py.com.purpleapps.yovotopy.ui.ExplorarFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyExplorarRecyclerViewAdapter extends RecyclerView.Adapter<MyExplorarRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Object> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyExplorarRecyclerViewAdapter(Context context, List<Object> items,
                                         OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_explorar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mItem instanceof Departamento) {
            Departamento departamento = (Departamento) holder.mItem;

            int currentItem = position % 4;

            setViewBackground(currentItem, holder.mView);

            holder.mIdView.setText(departamento.getNombre().substring(0, 1));
            holder.mContentView.setText(departamento.getNombre());
        } else if (holder.mItem instanceof Distrito) {
            Distrito distrito = (Distrito) holder.mItem;
            int currentItem = position % 4;

            setViewBackground(currentItem, holder.mView);

            holder.mIdView.setText(distrito.getNombre().substring(0, 1));
            holder.mContentView.setText(distrito.getNombre());
        } else if (holder.mItem instanceof Partido) {
            Partido partido = (Partido) holder.mItem;

            int currentItem = position % 4;

            setViewBackground(currentItem, holder.mView);

            holder.mIdView.setText(partido.getLista().toString());
            holder.mContentView.setText(partido.getNombre());
        } else if (holder.mItem instanceof Candidato) {
            Candidato candidato = (Candidato) holder.mItem;

            int currentItem = position % 4;

            setViewBackground(currentItem, holder.mView);

            holder.mIdView.setText(candidato.getLista().toString());
            holder.mContentView.setText(candidato.getNombreApellido());
        } else if (holder.mItem instanceof String) {
            String whatever = (String) holder.mItem;

            int currentItem = position % 4;

            setViewBackground(currentItem, holder.mView);

            holder.mIdView.setText(whatever.substring(0, 1));
            holder.mContentView.setText(whatever);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void addItems(List<Object> items) {
        mValues.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItems() {
        mValues.clear();
        notifyDataSetChanged();
    }

    private void setViewBackground(int itemPosition, View view) {
        try {
            if (itemPosition == 0) {
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.explorar_item_1));
            } else if (itemPosition == 1) {
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.explorar_item_2));
            } else if (itemPosition == 2) {
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.explorar_item_3));
            } else if (itemPosition == 3) {
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.explorar_item_4));
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Adapter", e.getLocalizedMessage());
            if (itemPosition == 0) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.materialLightPrimaryColor));
            } else if (itemPosition == 1) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.materialLightAccentColor));
            } else if (itemPosition == 2) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.materialAccentColor));
            } else if (itemPosition == 3) {
                view.setBackgroundColor(mContext.getResources().getColor(R.color.materialDarkPrimaryColor));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Object mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
