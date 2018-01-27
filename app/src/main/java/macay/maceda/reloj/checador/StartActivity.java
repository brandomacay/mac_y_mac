package macay.maceda.reloj.checador;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ImageButton btn_admin = (ImageButton) findViewById(R.id.admin);
        ImageButton btn_user = (ImageButton) findViewById(R.id.user);
        btn_admin.setOnClickListener(this);
        btn_user.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.admin:
                startActivity(new Intent(StartActivity.this,AdminActivity.class));
                break;
            case R.id.user:
                startActivity(new Intent(StartActivity.this,MainActivity.class));
                overridePendingTransition(0,0);
                break;
        }
    }
}
