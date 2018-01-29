package macay.maceda.reloj.checador.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import macay.maceda.reloj.checador.AdminActivity;
import macay.maceda.reloj.checador.EditUser;
import macay.maceda.reloj.checador.Model.Empleados_admin;
import macay.maceda.reloj.checador.R;

/**
 * Created by Vlover on 28/01/2018.
 */

public class User_detail_admin extends RecyclerView.Adapter<User_detail_admin.ViewHolder> {
    private List<Empleados_admin> mEmpleados;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personName;
        public TextView personPhone;
        public TextView personOcupation;
        public TextView personArea;
        public TextView personEmail;
        public TextView personBirthday;
        public TextView personAddress;
        public TextView personStartWork;
        public ImageView personImage;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personName = (TextView) v.findViewById(R.id.nombres);
            personPhone = (TextView) v.findViewById(R.id.celular);
            personEmail = (TextView) v.findViewById(R.id.correo);
            personOcupation = (TextView) v.findViewById(R.id.cargo);
            personArea = (TextView) v.findViewById(R.id.area);
            personBirthday = (TextView) v.findViewById(R.id.nacimiento);
            personAddress = (TextView) v.findViewById(R.id.direccion);
            personStartWork = (TextView) v.findViewById(R.id.iniciotrabajo);

            personImage = (ImageView) v.findViewById(R.id.fotouser);

        }
    }

    public void add(int position, Empleados_admin empleados_admin) {
        mEmpleados.add(position, empleados_admin);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mEmpleados.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public User_detail_admin(List<Empleados_admin> myDataset, Context context, RecyclerView recyclerView) {
        mEmpleados = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public User_detail_admin.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_recyclerview_users_by_admin, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Empleados_admin person = mEmpleados.get(position);
        holder.personName.setText(person.getName()+ " " + person.getLastname());
        holder.personPhone.setText(person.getNumber_phone());
        holder.personOcupation.setText(person.getOccupation());
        holder.personArea.setText(person.getArea());
        holder.personEmail.setText(person.getEmail());
        holder.personBirthday.setText(person.getBirthday());
        holder.personAddress.setText(person.getAddress());
        holder.personStartWork.setText(person.getDatework());

        if (!person.getImage().isEmpty()) {
            Picasso.with(mContext).load(new File(person.getImage())).placeholder(R.mipmap.ic_launcher).into(holder.personImage);

        }


        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void goToUpdateUser(long personId){
        Intent goToUpdate = new Intent(mContext, EditUser.class);
        goToUpdate.putExtra("USER_ID", personId);
        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEmpleados.size();
    }
}