package com.student.sharepreferenceapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class RegistrationFragment extends Fragment {
    private EditText fullName, emailAddress, createPassword;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view =inflater.inflate(R.layout.fragment_registration, container, false);

        Button signUp = view.findViewById(R.id.btn_signUp);
         fullName = view.findViewById(R.id.full_name);
        emailAddress = view.findViewById(R.id.create_email);
        createPassword = view.findViewById(R.id.create_password);
        TextView lnkLoginPage = view.findViewById(R.id.lnk_login);


        signUp.setOnClickListener(view1 -> confirmSignUp());

        lnkLoginPage.setOnClickListener(view12 -> startLogin());

        return view;
    }

    private void confirmSignUp(){
        String nameInput = fullName.getText().toString();
        String emailInput = emailAddress.getText().toString();
        String passInput = createPassword.getText().toString();
        String usernameREX ="^"+"(?=\\S+$)"+".{8,15}"+"$";
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";

        if (!TextUtils.isEmpty(nameInput) && !TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(passInput)){
            if (nameInput.matches(usernameREX)){
                if (Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                    if (passInput.matches(passwordVal)){
                        startRegistration(nameInput,emailInput, passInput);
                    }else{createPassword.setError(getString(R.string.password_error));
                       showPassDialog();}
                }else{emailAddress.setError(getString(R.string.emailAddress_error));
                    Toast.makeText(this.getActivity(), getString(R.string.email_order), Toast.LENGTH_LONG).show();}
            }else{fullName.setError(getString(R.string.username_error));
                showUserNameDialog();
            }
        }else {createPassword.setError(getString(R.string.fieldEmpty_error));
            emailAddress.setError(getString(R.string.fieldEmpty_error));
            fullName.setError(getString(R.string.fieldEmpty_error));}
    }

    private void startRegistration(String nameInput, String emailInput, String passInput){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("NAME",nameInput);
        editor.putString("EMAIL", emailInput);
        editor.putString("PASSWORD", passInput);
        editor.putBoolean("ISLOGGEDIN",true);
        editor.commit();
        startLogin();
    }

    private void startLogin(){
        LoginFragment newFragment = new LoginFragment();
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commit();
    }

    private void showUserNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this.getActivity()));
        builder.setTitle(R.string.username_error)
                .setMessage("Follow Order:\n* any letter\n* no white spaces\n* at least 8 characters")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(this.getActivity()));
        builder.setTitle(R.string.password_error)
                .setMessage("Follow Order : \n" +
                        "* any letter\n" +
                        "* no white spaces\n" +
                        "* at least 8 characters")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}