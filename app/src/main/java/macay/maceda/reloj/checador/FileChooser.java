package macay.maceda.reloj.checador;


import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.DateFormat;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.view.*;


public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    private int option=-1;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        option = intent.getIntExtra("option", -1);
        //	show_toast(""+ csv);
        if (option == 1) {
            currentDir = new File("/sdcard/vn/");
        }
        else if (option == 2) {
            currentDir = new File("/sdcard/checkprice/backup/");
        }
        else if (option == 3) {
            currentDir = new File("/sdcard/checkprice/background/");
        }
        else if (option == 4) {
            currentDir = new File("/sdcard/checkprice/photos/");
        }
        else if (option == 5) {
            currentDir = new File("/sdcard/");
        }
        else if (option == 6) {
            currentDir = new File("/sdcard/alexiapos/backup/");
        }
        else
            currentDir = new File("/sdcard/dcim/");
        fill(currentDir);
        if (savedInstanceState != null){

            //show_toast("Inicio con datos");
        }

    }

    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Item>dir = new ArrayList<Item>();
        List<Item>fls = new ArrayList<Item>();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(!ff.isHidden()) {
                    if(ff.isDirectory()){


                        File[] fbuf = ff.listFiles();
                        int buf = 0;
                        if(fbuf != null){
                            buf = fbuf.length;
                        }
                        else buf = 0;
                        String num_item = String.valueOf(buf);
                        if(buf == 0) num_item = num_item + " item";
                        else num_item = num_item + " items";

                        //String formated = lastModDate.toString();
                        dir.add(new Item(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                    }
                    else
                    {
                        if (option == 1) {
                            String filename = ff.getName();
                            if (filename.contains("txt"))
                                fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                        }
                        else if (option == 2) {
                            String filename = ff.getName();
                            if (filename.contains("csv"))
                                fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                        }
                        else if (option == 6) {
                            String filename = ff.getName();
                            if (filename.contains("sqlite"))
                                fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"data64"));
                        }
                        else {
                            String filename = ff.getName();
                            if (filename.contains("jpg") || filename.contains("png") || filename.contains("bmp") || filename.contains("JPG") || filename.contains("PNG") || filename.contains("BMP") )
                                fls.add(new Item(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"gallery_icon"));
                        }
                    }
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase(""))
            dir.add(0,new Item("..","Parent Directory","",f.getParent(),"directory_up"));
        adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Item o = adapter.getItem(position);
        if(o.getImage().equalsIgnoreCase("directory_icon")||o.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }
    private void onFileClick(Item o)
    {
        //	Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("GetPath",currentDir.toString()+"/");
        intent.putExtra("GetFileName",o.getName());

        setResult(RESULT_OK, intent);

        //	addProductFragment apfrag = (addProductFragment) (MainActivity).getFragmentManager().findFragmentByTag("addProductFrag");
        //	apfrag.handlephoto(data);
        finish();
    }
    public void show_toast (String texto) {
        Toast.makeText(this, texto,
                Toast.LENGTH_SHORT).show();
    }

}
