package org.anastdronina.gyperborea;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static org.anastdronina.gyperborea.ResetPreferences.ALL_SETTINGS;

public class PersonCard extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView personName, personSurname, personSalary, personAge, skillsAndTraits, date, moneyD, moneyR;
    private AlertDialog dialogName, dialogSurname;
    private TextView tvFoDialogChangeJob;
    private EditText editPersonName, editPersonSurname;
    private Spinner spinnerJobs;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayList skills;
    private int myId;
    private DateAndMoney dateAndMoney;
    private SharedPreferences allSettings;
    private String printCoef;
    private DbThread.DbListener listener;
    DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_card);

        dbManager = new DbManager();
        dateAndMoney = new DateAndMoney();
        personName = findViewById(R.id.personName);
        personSurname = findViewById(R.id.personSurname);
        personSalary = findViewById(R.id.personSalary);
        personAge = findViewById(R.id.personAge);
        skillsAndTraits = findViewById(R.id.skillsAndTraits);
        date = findViewById(R.id.date);
        moneyR = findViewById(R.id.moneyR);
        moneyD = findViewById(R.id.moneyD);
        dialogName = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogName.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogSurname = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogSurname.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        editPersonName = new EditText(this);
        editPersonSurname = new EditText(this);
        tvFoDialogChangeJob = new TextView(this);
        tvFoDialogChangeJob.setText(R.string.coef_will_be_cleared_wanna_continue);
        spinnerJobs = findViewById(R.id.spinnerJobs);
        adapter = ArrayAdapter.createFromResource(this, R.array.jobs, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobs.setAdapter(adapter);
        spinnerJobs.setOnItemSelectedListener(this);
        allSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        myId = allSettings.getInt("CURRENT_PERS_ID", 0);

        date.setText(dateAndMoney.getDate(allSettings));
        moneyD.setText(dateAndMoney.getMoney(allSettings, "$"));
        moneyR.setText(dateAndMoney.getMoney(allSettings, "руб"));

// setting name, surname and job
        personName.setText(allSettings.getString("CURRENT_PERS_NAME", ""));
        personSurname.setText(allSettings.getString("CURRENT_PERS_SURNAME", ""));
        spinnerJobs.setSelection(allSettings.getInt("CURRENT_PERS_JOB", 0));

// setting age
        int lastNum = (allSettings.getInt("CURRENT_PERS_AGE", 0)) % 10;
        String format;
        if (lastNum == 1) format = " год";
        else if (lastNum >= 2 && lastNum <= 4) format = " года";
        else format = " лет";
        personAge.setText((allSettings.getInt("CURRENT_PERS_AGE", 0)) + format);

// setting salary
        personSalary.setText(printSalary(allSettings));

// setting skills
        String printSkills = printSkills(allSettings);

//setting traits
        String printTraits = printTraits(allSettings);

//setting coefs if any
        printCoef(allSettings);
        skillsAndTraits.setText(printSkills + printTraits);

        dialogName.setTitle("Редактировать имя");
        dialogName.setView(editPersonName);
        dialogSurname.setTitle("Редактировать фамилию");
        dialogSurname.setView(editPersonSurname);

        dialogName.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                personName.setText(editPersonName.getText());
                dbManager.performQuery("UPDATE " + "population" + " SET NAME='" + editPersonName.getText() + "'WHERE ID='" + myId + "'");
            }
        });
        personName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPersonName.setText(personName.getText());
                dialogName.show();
            }
        });

        dialogSurname.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                personSurname.setText(editPersonSurname.getText());
                dbManager.performQuery("UPDATE " + "population" + " SET SURNAME='" + editPersonSurname.getText() + "'WHERE ID='" + myId + "'");
            }
        });

        personSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPersonSurname.setText(personSurname.getText());
                dialogSurname.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (allSettings.getInt("CURRENT_PERS_ID", 0) == allSettings.getInt("SCIENTIST_IN_USE_ID", 0)) {
            Toast.makeText(getApplicationContext(), R.string.scientist_busy, Toast.LENGTH_SHORT).show();
            spinnerJobs.setSelection(Arrays.asList((getResources().getStringArray(R.array.jobs))).indexOf("Ученый"));
        } else if (allSettings.getInt("CURRENT_PERS_ID", 0) == allSettings.getInt("FARMER_IN_USE_ID", 0)) {
            Toast.makeText(getApplicationContext(), R.string.farmer_busy, Toast.LENGTH_SHORT).show();
            spinnerJobs.setSelection(Arrays.asList((getResources().getStringArray(R.array.jobs))).indexOf("Фермер"));
        } else {
            changingJob(text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DbThread.getInstance().addListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(listener);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getJobStr(SharedPreferences allSettings) {
        int job = allSettings.getInt("CURRENT_PERS_JOB", 0);
        String myJob = "";
        switch (job) {
            case 0:
                myJob = "Не работает";
                break;
            case 1:
                myJob = "Строитель";
                break;
            case 2:
                myJob = "Сборщик";
                break;
            case 3:
                myJob = "Механик";
                break;
            case 4:
                myJob = "Фермер";
                break;
            case 5:
                myJob = "Курьер";
                break;
            case 6:
                myJob = "Ученый";
                break;
            case 7:
                myJob = "Министр";
                break;
            case 8:
                myJob = "Охранник";
                break;
            case 9:
                myJob = "Разработчик";
                break;
            case 10:
                myJob = "Уборщик";
                break;
            case 11:
                myJob = "Финансист";
                break;
            case 12:
                myJob = "Торговец";
                break;
            default:
        }
        return myJob;
    }

    public String printSalary(SharedPreferences allSettings) {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###,###,###,###");
        return decimalFormat.format(allSettings.getInt("CURRENT_PERS_SALARY", 0)) + " \u20BD";
    }

    public String printSkills(SharedPreferences allSettings) {
        skills = new ArrayList();
        if (allSettings.getInt("CURRENT_PERS_BUILDING", 0) > 0)
            skills.add("Строительство " + allSettings.getInt("CURRENT_PERS_BUILDING", 0));
        if (allSettings.getInt("CURRENT_PERS_MANUFACTURE", 0) > 0)
            skills.add("Мастерство " + allSettings.getInt("CURRENT_PERS_MANUFACTURE", 0));
        if (allSettings.getInt("CURRENT_PERS_FARM", 0) > 0)
            skills.add("Фермерство " + allSettings.getInt("CURRENT_PERS_FARM", 0));
        if (allSettings.getInt("CURRENT_PERS_ATHLETIC", 0) > 0)
            skills.add("Атлетичность " + allSettings.getInt("CURRENT_PERS_ATHLETIC", 0));
        if (allSettings.getInt("CURRENT_PERS_LEARNING", 0) > 0)
            skills.add("Обучение " + allSettings.getInt("CURRENT_PERS_LEARNING", 0));
        if (allSettings.getInt("CURRENT_PERS_TALKING", 0) > 0)
            skills.add("Переговоры " + allSettings.getInt("CURRENT_PERS_TALKING", 0));
        if (allSettings.getInt("CURRENT_PERS_STRENGTH", 0) > 0)
            skills.add("Сила " + allSettings.getInt("CURRENT_PERS_STRENGTH", 0));
        if (allSettings.getInt("CURRENT_PERS_ART", 0) > 0)
            skills.add("Творчество " + allSettings.getInt("CURRENT_PERS_ART", 0));

        String printSkills = "НАВЫКИ: \n\n";
        for (int i = 0; i < skills.size(); i++) {
            printSkills += skills.get(i) + "\n";
        }

        return printSkills;
    }

    public String printTraits(SharedPreferences allSettings) {
        String trait1 = allSettings.getString("CURRENT_PERS_TRAIT_1", "");
        String trait2 = allSettings.getString("CURRENT_PERS_TRAIT_2", "");
        String trait3 = allSettings.getString("CURRENT_PERS_TRAIT_3", "");
        String description1 = "", description2 = "", description3 = "";

//setting traits' descriptions...
        Traits traits = new Traits();
        String traitsList[] = traits.getTraits();
        String descriptionsList[] = traits.getDescriptions();
        for (int i = 0; i < traitsList.length; i++) {
            if (traitsList[i].equals(trait1)) {
                description1 = descriptionsList[i];
            }
            if (traitsList[i].equals(trait2)) {
                description2 = descriptionsList[i];
            }
            if (traitsList[i].equals(trait3)) {
                description3 = descriptionsList[i];
            }
        }
        return "\n\nЧЕРТЫ ХАРАКТЕРА : \n\n" + trait1 + "\n- " + description1 + "\n\n" + trait2 + "\n- " + description2 + "\n\n" + trait3 + "\n- " + description3 + "\n";
    }

    public void printCoef(SharedPreferences allSettings) {
        if (allSettings.getInt("CURRENT_PERS_JOB", Person.NOT_EMPLOYED) == Person.FINANSIST) {
            dbManager.printCoefAsync(allSettings.getInt("CURRENT_PERS_ID", 0));

            listener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundlee) {
                    printCoef = bundlee.getString("printCoef");
                    if (printCoef != null) {
                        if (!(skillsAndTraits.getText().toString().endsWith(printCoef))) {
                            skillsAndTraits.append(printCoef);
                        }
                    }
                }
            };
            DbThread.getInstance().addListener(listener);
        }
    }

    public void changingJob(String text) {
        if (text.equals("Не работает")) {
            if (!getJobStr(allSettings).equals("Не работает")) {
                showAlertDialogForChangingJob(Person.NOT_EMPLOYED, Person.NOT_EMPLOYED_SALARY);
            }
        }
        if (text.equals("Строитель")) {
            if (!getJobStr(allSettings).equals("Строитель")) {
                showAlertDialogForChangingJob(Person.BUILDER, Person.BUILDER_SALARY);
            }
        }
        if (text.equals("Сборщик")) {
            if (!getJobStr(allSettings).equals("Сборщик")) {
                showAlertDialogForChangingJob(Person.COLLECTOR, Person.COLLECTOR_SALARY);
            }
        }
        if (text.equals("Механик")) {
            if (!getJobStr(allSettings).equals("Механик")) {
                showAlertDialogForChangingJob(Person.MECHANIC, Person.MECHANIC_SALARY);
            }
        }
        if (text.equals("Фермер")) {
            if (!getJobStr(allSettings).equals("Фермер")) {
                showAlertDialogForChangingJob(Person.FARMER, Person.FARMER_SALARY);
            }
        }
        if (text.equals("Курьер")) {
            if (!getJobStr(allSettings).equals("Курьер")) {
                showAlertDialogForChangingJob(Person.COURIER, Person.COURIER_SALARY);
            }
        }
        if (text.equals("Ученый")) {
            if (!getJobStr(allSettings).equals("Ученый")) {
                showAlertDialogForChangingJob(Person.SCIENTIST, Person.SCIENTIST_SALARY);
            }
        }
        if (text.equals("Министр")) {
            if (!getJobStr(allSettings).equals("Министр")) {
                showAlertDialogForChangingJob(Person.MINISTER, Person.MINISTER_SALARY);
            }
        }
        if (text.equals("Охранник")) {
            if (!getJobStr(allSettings).equals("Охранник")) {
                showAlertDialogForChangingJob(Person.SECURITY, Person.SECURITY_SALARY);
            }
        }
        if (text.equals("Разработчик")) {
            if (!getJobStr(allSettings).equals("Разработчик")) {
                showAlertDialogForChangingJob(Person.DEVELOPER, Person.DEVELOPER_SALARY);
            }
        }
        if (text.equals("Уборщик")) {
            if (!getJobStr(allSettings).equals("Уборщик")) {
                showAlertDialogForChangingJob(Person.CLEANER, Person.CLEANER_SALARY);
            }
        }
        if (text.equals("Финансист")) {
            if (!getJobStr(allSettings).equals("Финансист")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
                alertDialogBuilder
                        .setTitle(R.string.current_coef_will_be_deleted)
                        .setCancelable(false)
                        .setPositiveButton("Oк", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dbManager.performQuery("UPDATE " + "population" + " SET JOB='" + Person.FINANSIST + "'WHERE ID='" + myId + "'");
                                dbManager.performQuery("UPDATE " + "population" + " SET SALARY='" + Person.FINANSIST_SALARY + "'WHERE ID='" + myId + "'");

                                if (allSettings.getInt("CURRENT_PERS_LEARNING", 0) > 0) {
                                    double coef = 0.2 * allSettings.getInt("CURRENT_PERS_LEARNING", 0);
                                    dbManager.performQuery("UPDATE " + "population" + " SET FIN_COEF='" + coef + "'WHERE ID='" + myId + "'");
                                }
                                allSettings.edit().putInt("CURRENT_PERS_SALARY", Person.FINANSIST_SALARY).apply();
                                allSettings.edit().putInt("CURRENT_PERS_JOB", Person.FINANSIST).apply();
                                personSalary.setText(printSalary(allSettings));
                                String printSkills = printSkills(allSettings);
                                String printTraits = printTraits(allSettings);
                                printCoef(allSettings);
                                skillsAndTraits.setText(printSkills + printTraits);
                            }
                        })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
                alertDialog.show();
            }
        }
        if (text.equals("Торговец")) {
            if (!getJobStr(allSettings).equals("Торговец")) {
                showAlertDialogForChangingJob(Person.SALESMAN, Person.SALESMAN_SALARY);
            }
        }
    }

    public void showAlertDialogForChangingJob(final int job, final int salary) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alertDialogBuilder
                .setTitle(R.string.current_coef_will_be_deleted)
                .setCancelable(false)
                .setPositiveButton("Oк", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbManager.performQuery("UPDATE " + "population" + " SET JOB='" + job + "'WHERE ID='" + myId + "'");
                        dbManager.performQuery("UPDATE " + "population" + " SET SALARY='" + salary + "'WHERE ID='" + myId + "'");
                        dbManager.performQuery("UPDATE " + "population" + " SET FIN_COEF='" + 0.0 + "'WHERE ID='" + myId + "'");
                        allSettings.edit().putInt("CURRENT_PERS_SALARY", salary).apply();
                        allSettings.edit().putInt("CURRENT_PERS_JOB", job).apply();
                        personSalary.setText(printSalary(allSettings));
                        String printSkills = printSkills(allSettings);
                        String printTraits = printTraits(allSettings);
                        printCoef(allSettings);
                        skillsAndTraits.setText(printSkills + printTraits);
                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                spinnerJobs.setSelection(allSettings.getInt("CURRENT_PERS_JOB", Person.NOT_EMPLOYED));
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        alertDialog.show();
    }
}
