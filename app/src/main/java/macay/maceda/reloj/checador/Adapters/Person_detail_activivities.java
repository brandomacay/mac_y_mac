package macay.maceda.reloj.checador.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.EditUser;
import macay.maceda.reloj.checador.Model.Actividades_empleados;
import macay.maceda.reloj.checador.Model.Empleados_admin;
import macay.maceda.reloj.checador.R;

/**
 * Created by Vlover on 06/02/2018.
 */

public class Person_detail_activivities extends RecyclerView.Adapter<Person_detail_activivities.ViewHolder> {
    private List<Actividades_empleados> mEmpleados;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView personId;
        TextView personUserId;
        TextView personWorking;
        TextView personWorkout;
        TextView personBreaking;
        TextView personBreakout;
        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            personId =  (TextView) v.findViewById(R.id.a_ide);
            personUserId = (TextView) v.findViewById(R.id.user_id);
            personWorking = (TextView) v.findViewById(R.id.a_working);
            personWorkout = (TextView) v.findViewById(R.id.a_workout);
            personBreaking = (TextView) v.findViewById(R.id.a_breaking);
            personBreakout = (TextView) v.findViewById(R.id.a_breakout);

        }


    }

    public void add(int position, Actividades_empleados empleados_admin) {
        mEmpleados.add(position, empleados_admin);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mEmpleados.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public Person_detail_activivities(List<Actividades_empleados> myDataset, Context context, RecyclerView recyclerView) {
        mEmpleados = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Person_detail_activivities.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_actividades_empleados, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Person_detail_activivities.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Actividades_empleados person = mEmpleados.get(position);
        holder.personId.setText("Numero:"+ person.getId());
        holder.personUserId.setText("ID empleado: " +person.getUserid());
        holder.personWorking.setText("Inicio de trabajo: "+person.getWorking());
        holder.personWorkout.setText("Culminacion de Trabajo: "+person.getWorkout());
        holder.personBreaking.setText("Inicio de comida: "+person.getBreaking());
        holder.personBreakout.setText("Salida de comida: "+person.getBreakout());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }
    @Override
    public int getItemCount() {
        return mEmpleados.size();
    }
}