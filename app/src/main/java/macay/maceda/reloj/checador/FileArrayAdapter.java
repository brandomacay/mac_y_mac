package macay.maceda.reloj.checador;


import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class FileArrayAdapter extends ArrayAdapter<Item>{

    private Context c;
    private int id;
    private List<Item>items;

    public FileArrayAdapter(Context context, int textViewResourceId,
                            List<Item> objects) {
        super(context, textViewResourceId, objects);
        c = context;
        id = textViewResourceId;
        items = objects;
    }
    public Item getItem(int i)
    {
        return items.get(i);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

               /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        final Item o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.TextView01);
            TextView t2 = (TextView) v.findViewById(R.id.TextView02);
            TextView t3 = (TextView) v.findViewById(R.id.TextViewDate);
                       /* Take the ImageView from layout and set the city's image */
            ImageView imageCity = (ImageView) v.findViewById(R.id.fd_Icon1);




            if (!o.getImage().equals("gallery_icon")) {

                String uri = "drawable/" + o.getImage();
                int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
                Drawable image = c.getResources().getDrawable(imageResource);
                imageCity.requestLayout();
                //	imageCity.
                imageCity.getLayoutParams().height = 70;
                imageCity.getLayoutParams().width = 70;
                //imageCity.setLayoutParams(new LayoutParams(l
                //RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50, 50);
                //lp.
                //lp.gravity = Gravity.CENTER;
                //imageCity.setLayoutParams(lp);
                imageCity.setImageDrawable(image);
            }
            else {
                imageCity.requestLayout();
                imageCity.getLayoutParams().height = 140;
                imageCity.getLayoutParams().width = 140;


                Picasso.with(c)
                        .load(new File(o.getPath()))
                        .fit()
                        //.resize(600,6000)
                        .centerInside()
                        //.placeholder(R.drawable.agregar_imagen)
                        //.error(R.drawable.vlover)
                        //.networkPolicy(NetworkPolicy.NO_STORE)
                        //.memoryPolicy(MemoryPolicy.NO_STORE)
                        .into(imageCity);

                /*
                //imageCity.setImageBitmap(BitmapFactory.decodeFile(o.getPath() ));
                imageCity.requestLayout();
                //	imageCity.
                imageCity.getLayoutParams().height = 140;
                imageCity.getLayoutParams().width = 140;



                int targetW = 50;
                int targetH = 50;

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(o.getPath(), bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
                // to here


                //delete
                //BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                //bmOptions.inSampleSize = 8;
                bmOptions.inPurgeable = true;


                Bitmap bitmap = BitmapFactory.decodeFile(o.getPath(), bmOptions);
                imageCity.setImageBitmap(bitmap);
                */
            }
            if(t1!=null)
                t1.setText(o.getName());
            if(t2!=null)
                t2.setText(o.getData());
            if(t3!=null)
                t3.setText(o.getDate());

        }
        return v;
    }



}
