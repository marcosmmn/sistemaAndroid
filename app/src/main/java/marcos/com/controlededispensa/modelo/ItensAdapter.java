package marcos.com.controlededispensa.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import marcos.com.controlededispensa.R;

public class ItensAdapter extends BaseAdapter {

    Context context;
    List<Categoria> itens;

    @Override
    public int getCount() {
        return  itens.size();
    }

    @Override
    public Object getItem(int i) {
        return itens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CategoriaHolder holder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_produtos,viewGroup,false);

            holder = new CategoriaHolder();

            holder.imageViewItem = view.findViewById(R.id.imageViewProduto);
            holder.textViewNome = view.findViewById(R.id.textViewNome);

            view.setTag(holder);
        }else{
            holder = (CategoriaHolder) view.getTag();
        }

        holder.imageViewItem.setImageDrawable(itens.get(i).getIcone());
        holder.textViewNome.setText(itens.get(i).getDescricao());

        return view;
    }

    private static class CategoriaHolder{
        public ImageView imageViewItem;
        public TextView textViewNome;
    }

    public ItensAdapter(Context context, List<Categoria> itens){
    this.context=context;
        this.itens = itens;
    }

}
