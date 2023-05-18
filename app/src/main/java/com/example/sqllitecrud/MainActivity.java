package com.example.sqllitecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText STUDENT_ID,studentName,studentGrade;
    SQLiteDatabase database;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        STUDENT_ID = findViewById(R.id.studentID);
        studentGrade = findViewById(R.id.studentGrade);
        studentName = findViewById(R.id.studentName);


        database = openOrCreateDatabase("university", Context.MODE_PRIVATE,null);

        database.execSQL("CREATE TABLE IF NOT EXISTS" +
                " student_table(student_id VARCHAR PRIMARY KEY, " +
                "student_name VARCHAR NOT NULL," +
                " student_grade DOUBLE)");
    }

    public void addData(View view){
        database.execSQL("INSERT INTO student_table " +
                "VALUES('"+STUDENT_ID.getEditableText().toString()+"'," +
                "'"+studentName.getEditableText().toString()+"'," +
                "'"+Double.parseDouble(studentGrade.getEditableText().toString())+"')");
        Toast.makeText(this,"NEW DATA SUCCESSFULLY SAVE", Toast.LENGTH_LONG).show();
    }
    public void updateData(View view) {
        database.execSQL("UPDATE student_table " +
                "SET student_name = '" + studentName.getEditableText().toString() + "'," +
                " student_grade = '" + Double.parseDouble(studentGrade.getEditableText().toString()) + "'" +
                " WHERE student_id = '" + STUDENT_ID.getEditableText().toString() + "'");

        Toast.makeText(this, "UPDATE DATA SUCCESSFULLY", Toast.LENGTH_LONG).show();
    }

    public void deleteData(View view) {
        String studentId = STUDENT_ID.getEditableText().toString();

        int rowsDeleted = database.delete("student_table", "student_id = ?", new String[]{studentId});
        if (rowsDeleted > 0) {
            STUDENT_ID.setText("");
            studentName.setText("");
            studentGrade.setText("");
            Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed to delete data", Toast.LENGTH_LONG).show();
        }
    }

    public void returnData(String id,String name,String grade){
        STUDENT_ID.setText(id);
        studentName.setText(name);
        studentGrade.setText(grade);
    }
    public void viewAllData(View view) {
        Cursor cursor = database.rawQuery("SELECT * FROM student_table", null);

        StringBuilder data = new StringBuilder();
        if (cursor.moveToFirst()) {
            int studentIdIndex = cursor.getColumnIndex("student_id");
            int studentNameIndex = cursor.getColumnIndex("student_name");
            int studentGradeIndex = cursor.getColumnIndex("student_grade");

            do {
                String studentId = cursor.getString(studentIdIndex);
                String studentName = cursor.getString(studentNameIndex);
                double studentGrade = cursor.getDouble(studentGradeIndex);

                data.append("Student ID: ").append(studentId).append("\n");
                data.append("Student Name: ").append(studentName).append("\n");
                data.append("Student Grade: ").append(studentGrade).append("\n\n");
            } while (cursor.moveToNext());
        }

        if (data.length() > 0) {
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        }

        cursor.close();
    }

    public void searchData(View view) {
        String studentId = STUDENT_ID.getEditableText().toString();

        cursor = database.rawQuery("SELECT * FROM student_table WHERE student_id = ?", new String[]{studentId});

        StringBuilder data = new StringBuilder();
        if (cursor.moveToFirst()) {
            int studentNameIndex = cursor.getColumnIndex("student_name");
            int studentGradeIndex = cursor.getColumnIndex("student_grade");

            do {
                String studentName = cursor.getString(studentNameIndex);
                double studentGrade = cursor.getDouble(studentGradeIndex);

                data.append("Student ID: ").append(studentId).append("\n");
                data.append("Student Name: ").append(studentName).append("\n");
                data.append("Student Grade: ").append(studentGrade).append("\n\n");

                returnData(studentId,studentName,String.valueOf(studentGrade));

            } while (cursor.moveToNext());
        }

        if (data.length() > 0) {
            Toast.makeText(this, data.toString(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show();
        }

        cursor.close();
    }


}