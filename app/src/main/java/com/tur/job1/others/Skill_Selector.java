package com.tur.job1.others;



import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tur.job1.R;
import com.tur.job1.adapters.SkillsSetAdapter;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class Skill_Selector extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] options;

    ArrayList<String> selectedSkills = new ArrayList<>();
    private ListView skillView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_input);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        skillView = (ListView)findViewById(R.id.skill_list);
        // Creating ArrayAdapter using the string array and default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Animals, android.R.layout.simple_spinner_item);
        // Specify layout to be used when list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Applying the adapter to our spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        options = Skill_Selector.this.getResources().getStringArray(R.array.Animals);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Toast.makeText(this, " You select >> "+options[position], Toast.LENGTH_SHORT).show();
        selectedSkills.add(options[position]);

        skillView.setAdapter(new SkillsSetAdapter(this,selectedSkills.size(),selectedSkills, selectedSkills));
        //skillView.notify();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}