package macay.maceda.reloj.checador.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import macay.maceda.reloj.checador.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checador.DetailPersonActivity;
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
    private int screenWidth;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView personId;
        TextView personName;
        TextView personPhone;
        TextView personOcupation;
        TextView personArea;
        TextView personEmail;
        TextView personBirthday;
        TextView personAddress;
        TextView personStartWork;
        ImageView personImage;




        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            personId =  (TextView) v.findViewById(R.id.user_id);
            personName = (TextView) v.findViewById(R.id.nombres);
            personPhone = (TextView) v.findViewById(R.id.celular);
            personEmail = (TextView) v.findViewById(R.id.correo);
            personOcupation = (TextView) v.findViewById(R.id.cargo);
            personArea = (TextView) v.findViewById(R.id.area);
            personBirthday = (TextView) v.findViewById(R.id.nacimiento);
            personAddress = (TextView) v.findViewById(R.id.direccion);
            personStartWork = (TextView) v.findViewById(R.id.iniciotrabajo);
            personImage = (ImageView) v.findViewById(R.id.fotouser);
            personImage.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.fotouser:
                    Empleados_admin person1 = mEmpleados.get(getPosition());




                    if (person1.getImage().isEmpty()) {
                       // Empleados_admin person = mEmpleados.get(getPosition());
                        goToUpdateActivity(person1.getId());

                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                        builder.setNeutralButton("Editar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(mContext, "Yes button Clicked", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                                Empleados_admin person = mEmpleados.get(getPosition());
                                goToUpdateActivity(person.getId());
                            }
                        });


                        builder.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //  Toast.makeText(mContext, "Yes button Clicked", Toast.LENGTH_LONG).show();

                                dialog.dismiss();
                            }
                        });


                        LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        // LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View dialoglayout = li != null ? li.inflate(R.layout.image_dialog, null) : null;

                        ImageView imv = (ImageView) (dialoglayout != null ? dialoglayout.findViewById(R.id.user_dialog_imageView) : null);
                        String path = personImage.getTag().toString();

                        int height;
                        if (getPosition() == 1 || getPosition() == (mEmpleados.size() - 1)) {
                            height = 700;
                        } else {
                            height = 600;
                        }
                            Picasso.with(mContext)
                                    .load(new File(path))
                                    .resize(screenWidth / 2, height)
                                    .placeholder(R.drawable.persona)
                                    .into(imv);

                        builder.setView(dialoglayout);
                        builder.show();
                    }




                    break;

            }

        }
        private void goToUpdateActivity(long personId){
            Intent goToUpdate = new Intent(mContext, EditUser.class);
            goToUpdate.putExtra("USER_ID",personId);
            mContext.startActivity(goToUpdate);
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
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Empleados_admin person = mEmpleados.get(position);
        holder.personName.setText(person.getName()+ " " + person.getLastname());
        if(person.getNumber_phone().equals("")){
            holder.personPhone.setVisibility(View.GONE);
        }else{
            holder.personPhone.setText("Numero celular: "+person.getNumber_phone());

        }

        if (person.getOccupation().equals("")){
            holder.personOcupation.setVisibility(View.GONE);
        }else{
            holder.personOcupation.setText("Cargo/puesto: "+person.getOccupation());

        }
        if (person.getArea().equals("")){
            holder.personArea.setVisibility(View.GONE);
        }else{
            holder.personArea.setText("Area de trabajo: "+person.getArea());

        }
        if (person.getEmail().equals("")){
            holder.personEmail.setVisibility(View.GONE);
        }else{
            holder.personEmail.setText("Correo electronico: "+person.getEmail());

        }
        if (person.getBirthday().equals("")){
            holder.personBirthday.setVisibility(View.GONE);
        }else{
            holder.personBirthday.setText("Fecha de nacimiento: "+person.getBirthday());

        }
        if (person.getAddress().equals("")){
            holder.personAddress.setVisibility(View.GONE);
        }else{
            holder.personAddress.setText("Direccion: "+person.getAddress());

        }
        holder.personStartWork.setText("Fecha de contrato: "+person.getDatework());
        holder.personId.setText("ID empleado: " +person.getId());


        int height;
        if (position == 1 || position == (mEmpleados.size() - 1)) {
            height = 700;
        } else {
            height = 600;
        }
        if (!person.getImage().isEmpty()) {
            Picasso.with(mContext)
                    .load(new File(person.getImage()))
                    .resize(screenWidth / 2, height)
                    .placeholder(R.drawable.persona)
                    .into(holder.personImage);
            holder.personImage.setTag(person.getImage());

        }


        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsPerson(Long.parseLong(""+person.getId()));
            }
        });


    }


    private void goToDetailsPerson(long personId){
        Intent goToUpdate = new Intent(mContext, DetailPersonActivity.class);
        goToUpdate.putExtra("USER_ID", personId);
        mContext.startActivity(goToUpdate);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEmpleados.size();
    }
}