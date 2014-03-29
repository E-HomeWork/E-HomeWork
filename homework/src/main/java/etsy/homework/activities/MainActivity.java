package etsy.homework.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import etsy.homework.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private AdapterView<?> mAdapterView;
    private Button mButton;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapterView = (AdapterView<?>) findViewById(R.id.activity_main_list_view_search);
        mEditText = (EditText) findViewById(R.id.activity_main_edit_text_search);
        mButton = (Button) findViewById(R.id.activity_main_button_search);
        mButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mButton.setOnClickListener(null);
        mButton = null;
        mEditText = null;
        mAdapterView = null;
    }

    @Override
    public void onClick(final View view) {
        final String keyword = mEditText.getText().toString();
    }
}
