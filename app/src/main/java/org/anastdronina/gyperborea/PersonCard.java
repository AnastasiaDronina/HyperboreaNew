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

    private TextView tvPersonName;
    private TextView tvPersonSurname;
    private TextView tvPersonSalary;
    private TextView tvPersonAge;
    private TextView tvSkillsAndTraits;
    private TextView tvDate;
    private TextView tvMoneyD;
    private TextView tvMmoneyR;
    private TextView tvFoDialogChangeJob;
    private AlertDialog dialogName;
    private AlertDialog dialogSurname;
    private EditText etPersonName;
    private EditText etPersonSurname;
    private Spinner spinnerJobs;
    private ArrayAdapter<CharSequence> mAdapter;
    private int mId;
    private DateAndMoney mDateAndMoney;
    private SharedPreferences mSettings;
    private String mPrintCoef;
    private DbThread.DbListener mListener;
    private DbManager mDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_card);

        mDbManager = new DbManager();
        mDateAndMoney = new DateAndMoney();
        tvPersonName = findViewById(R.id.personName);
        tvPersonSurname = findViewById(R.id.personSurname);
        tvPersonSalary = findViewById(R.id.personSalary);
        tvPersonAge = findViewById(R.id.personAge);
        tvSkillsAndTraits = findViewById(R.id.skillsAndTraits);
        tvDate = findViewById(R.id.date);
        tvMmoneyR = findViewById(R.id.moneyR);
        tvMoneyD = findViewById(R.id.moneyD);
        dialogName = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogName.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        dialogSurname = new AlertDialog.Builder(this, R.style.MyDialogTheme).create();
        dialogSurname.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        etPersonName = new EditText(this);
        etPersonSurname = new EditText(this);
        tvFoDialogChangeJob = new TextView(this);
        tvFoDialogChangeJob.setText(R.string.coef_will_be_cleared_wanna_continue);
        spinnerJobs = findViewById(R.id.spinnerJobs);
        mAdapter = ArrayAdapter.createFromResource(this, R.array.jobs, R.layout.spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobs.setAdapter(mAdapter);
        spinnerJobs.setOnItemSelectedListener(this);
        mSettings = getSharedPreferences(ALL_SETTINGS, MODE_PRIVATE);
        mId = mSettings.getInt("CURRENT_PERS_ID", 0);

        tvDate.setText(mDateAndMoney.getDate(mSettings));
        tvMoneyD.setText(mDateAndMoney.getMoney(mSettings, "$"));
        tvMmoneyR.setText(mDateAndMoney.getMoney(mSettings, "руб"));

// setting name, surname and job
        tvPersonName.setText(mSettings.getString("CURRENT_PERS_NAME", ""));
        tvPersonSurname.setText(mSettings.getString("CURRENT_PERS_SURNAME", ""));
        spinnerJobs.setSelection(mSettings.getInt("CURRENT_PERS_JOB", 0));

// setting age
        int lastNum = (mSettings.getInt("CURRENT_PERS_AGE", 0)) % 10;
        String format;
        if (lastNum == 1) format = " год";
        else if (lastNum >= 2 && lastNum <= 4) format = " года";
        else format = " лет";
        tvPersonAge.setText((mSettings.getInt("CURRENT_PERS_AGE", 0)) + format);

// setting salary
        tvPersonSalary.setText(printSalary(mSettings));

// setting skills
        String printSkills = printSkills(mSettings);

//setting traits
        String printTraits = printTraits(mSettings);

//setting coefs if any
        printCoef(mSettings);
        tvSkillsAndTraits.setText(printSkills + printTraits);

        dialogName.setTitle("Редактировать имя");
        dialogName.setView(etPersonName);
        dialogSurname.setTitle("Редактировать фамилию");
        dialogSurname.setView(etPersonSurname);

        dialogName.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvPersonName.setText(etPersonName.getText());
                mDbManager.performQuery("UPDATE " + "population" + " SET NAME='" + etPersonName.getText()
                        + "'WHERE ID='" + mId + "'");
            }
        });
        tvPersonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPersonName.setText(tvPersonName.getText());
                dialogName.show();
            }
        });

        dialogSurname.setButton(DialogInterface.BUTTON_POSITIVE, "Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvPersonSurname.setText(etPersonSurname.getText());
                mDbManager.performQuery("UPDATE " + "population" + " SET SURNAME='" + etPersonSurname.getText()
                        + "'WHERE ID='" + mId + "'");
            }
        });

        tvPersonSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPersonSurname.setText(tvPersonSurname.getText());
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
        if (mSettings.getInt("CURRENT_PERS_ID", 0) == mSettings.getInt("SCIENTIST_IN_USE_ID", 0)) {
            Toast.makeText(getApplicationContext(), R.string.scientist_busy, Toast.LENGTH_SHORT).show();
            spinnerJobs.setSelection(Arrays.asList((getResources().getStringArray(R.array.jobs))).indexOf("Ученый"));
        } else if (mSettings.getInt("CURRENT_PERS_ID", 0) == mSettings.getInt("FARMER_IN_USE_ID", 0)) {
            Toast.makeText(getApplicationContext(), R.string.farmer_busy, Toast.LENGTH_SHORT).show();
            spinnerJobs.setSelection(Arrays.asList((getResources().getStringArray(R.array.jobs))).indexOf("Фермер"));
        } else {
            changingJob(text);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DbThread.getInstance().addListener(mListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DbThread.getInstance().removeListener(mListener);
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
        ArrayList skillsList = new ArrayList();
        if (allSettings.getInt("CURRENT_PERS_BUILDING", 0) > 0)
            skillsList.add("Строительство " + allSettings.getInt("CURRENT_PERS_BUILDING", 0));
        if (allSettings.getInt("CURRENT_PERS_MANUFACTURE", 0) > 0)
            skillsList.add("Мастерство " + allSettings.getInt("CURRENT_PERS_MANUFACTURE", 0));
        if (allSettings.getInt("CURRENT_PERS_FARM", 0) > 0)
            skillsList.add("Фермерство " + allSettings.getInt("CURRENT_PERS_FARM", 0));
        if (allSettings.getInt("CURRENT_PERS_ATHLETIC", 0) > 0)
            skillsList.add("Атлетичность " + allSettings.getInt("CURRENT_PERS_ATHLETIC", 0));
        if (allSettings.getInt("CURRENT_PERS_LEARNING", 0) > 0)
            skillsList.add("Обучение " + allSettings.getInt("CURRENT_PERS_LEARNING", 0));
        if (allSettings.getInt("CURRENT_PERS_TALKING", 0) > 0)
            skillsList.add("Переговоры " + allSettings.getInt("CURRENT_PERS_TALKING", 0));
        if (allSettings.getInt("CURRENT_PERS_STRENGTH", 0) > 0)
            skillsList.add("Сила " + allSettings.getInt("CURRENT_PERS_STRENGTH", 0));
        if (allSettings.getInt("CURRENT_PERS_ART", 0) > 0)
            skillsList.add("Творчество " + allSettings.getInt("CURRENT_PERS_ART", 0));

        String printSkills = "НАВЫКИ: \n\n";
        for (int i = 0; i < skillsList.size(); i++) {
            printSkills += skillsList.get(i) + "\n";
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
            mDbManager.printCoefAsync(allSettings.getInt("CURRENT_PERS_ID", 0));

            mListener = new DbThread.DbListener() {
                @Override
                public void onDataLoaded(Bundle bundlee) {
                    mPrintCoef = bundlee.getString("printCoef");
                    if (mPrintCoef != null) {
                        if (!(tvSkillsAndTraits.getText().toString().endsWith(mPrintCoef))) {
                            tvSkillsAndTraits.append(mPrintCoef);
                        }
                    }
                }
            };
            DbThread.getInstance().addListener(mListener);
        }
    }

    public void changingJob(String text) {
        if (text.equals("Не работает")) {
            if (!getJobStr(mSettings).equals("Не работает")) {
                showAlertDialogForChangingJob(Person.NOT_EMPLOYED, Person.NOT_EMPLOYED_SALARY);
            }
        }
        if (text.equals("Строитель")) {
            if (!getJobStr(mSettings).equals("Строитель")) {
                showAlertDialogForChangingJob(Person.BUILDER, Person.BUILDER_SALARY);
            }
        }
        if (text.equals("Сборщик")) {
            if (!getJobStr(mSettings).equals("Сборщик")) {
                showAlertDialogForChangingJob(Person.COLLECTOR, Person.COLLECTOR_SALARY);
            }
        }
        if (text.equals("Механик")) {
            if (!getJobStr(mSettings).equals("Механик")) {
                showAlertDialogForChangingJob(Person.MECHANIC, Person.MECHANIC_SALARY);
            }
        }
        if (text.equals("Фермер")) {
            if (!getJobStr(mSettings).equals("Фермер")) {
                showAlertDialogForChangingJob(Person.FARMER, Person.FARMER_SALARY);
            }
        }
        if (text.equals("Курьер")) {
            if (!getJobStr(mSettings).equals("Курьер")) {
                showAlertDialogForChangingJob(Person.COURIER, Person.COURIER_SALARY);
            }
        }
        if (text.equals("Ученый")) {
            if (!getJobStr(mSettings).equals("Ученый")) {
                showAlertDialogForChangingJob(Person.SCIENTIST, Person.SCIENTIST_SALARY);
            }
        }
        if (text.equals("Министр")) {
            if (!getJobStr(mSettings).equals("Министр")) {
                showAlertDialogForChangingJob(Person.MINISTER, Person.MINISTER_SALARY);
            }
        }
        if (text.equals("Охранник")) {
            if (!getJobStr(mSettings).equals("Охранник")) {
                showAlertDialogForChangingJob(Person.SECURITY, Person.SECURITY_SALARY);
            }
        }
        if (text.equals("Разработчик")) {
            if (!getJobStr(mSettings).equals("Разработчик")) {
                showAlertDialogForChangingJob(Person.DEVELOPER, Person.DEVELOPER_SALARY);
            }
        }
        if (text.equals("Уборщик")) {
            if (!getJobStr(mSettings).equals("Уборщик")) {
                showAlertDialogForChangingJob(Person.CLEANER, Person.CLEANER_SALARY);
            }
        }
        if (text.equals("Финансист")) {
            if (!getJobStr(mSettings).equals("Финансист")) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
                alertDialogBuilder
                        .setTitle(R.string.current_coef_will_be_deleted)
                        .setCancelable(false)
                        .setPositiveButton("Oк", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDbManager.performQuery("UPDATE " + "population" + " SET JOB='" + Person.FINANSIST
                                        + "'WHERE ID='" + mId + "'");
                                mDbManager.performQuery("UPDATE " + "population" + " SET SALARY='" + Person.FINANSIST_SALARY
                                        + "'WHERE ID='" + mId + "'");

                                if (mSettings.getInt("CURRENT_PERS_LEARNING", 0) > 0) {
                                    double coef = 0.2 * mSettings.getInt("CURRENT_PERS_LEARNING", 0);
                                    mDbManager.performQuery("UPDATE " + "population" + " SET FIN_COEF='" + coef
                                            + "'WHERE ID='" + mId + "'");
                                }
                                mSettings.edit().putInt("CURRENT_PERS_SALARY", Person.FINANSIST_SALARY).apply();
                                mSettings.edit().putInt("CURRENT_PERS_JOB", Person.FINANSIST).apply();
                                tvPersonSalary.setText(printSalary(mSettings));
                                String printSkills = printSkills(mSettings);
                                String printTraits = printTraits(mSettings);
                                printCoef(mSettings);
                                tvSkillsAndTraits.setText(printSkills + printTraits);
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
            if (!getJobStr(mSettings).equals("Торговец")) {
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
                        mDbManager.performQuery("UPDATE " + "population" + " SET JOB='" + job + "'WHERE ID='" + mId + "'");
                        mDbManager.performQuery("UPDATE " + "population" + " SET SALARY='" + salary + "'WHERE ID='" + mId + "'");
                        mDbManager.performQuery("UPDATE " + "population" + " SET FIN_COEF='" + 0.0 + "'WHERE ID='" + mId + "'");
                        mSettings.edit().putInt("CURRENT_PERS_SALARY", salary).apply();
                        mSettings.edit().putInt("CURRENT_PERS_JOB", job).apply();
                        tvPersonSalary.setText(printSalary(mSettings));
                        String printSkills = printSkills(mSettings);
                        String printTraits = printTraits(mSettings);
                        printCoef(mSettings);
                        tvSkillsAndTraits.setText(printSkills + printTraits);
                    }
                })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                spinnerJobs.setSelection(mSettings.getInt("CURRENT_PERS_JOB", Person.NOT_EMPLOYED));
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.MyDialogTheme;
        alertDialog.show();
    }
}
