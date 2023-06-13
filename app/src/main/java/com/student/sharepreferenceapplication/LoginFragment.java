package com.student.sharepreferenceapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private static final String TAG = "requiredDetails";
    private EditText inputEmail, inputPassword;
private SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_login, container, false);

       sharedPreferences = this.getActivity().getSharedPreferences("USER_CREDENTIALS", Context.MODE_PRIVATE);
//       final Boolean isloggedin = sharedPreferences.getBoolean("ISLOGEEDIN",false);
       final String required_name = sharedPreferences.getString("NAME","DEFAULT_NAME");
       final String required_email = sharedPreferences.getString("EMAIL","DEFAULT_EMAIL");
       final String required_password = sharedPreferences.getString("PASSWORD","DEFAULT_PASSWORD");

        Log.d(TAG, "required email= " + required_email);
        Log.d(TAG, "required name= " + required_name);
        Log.d(TAG, "required password= " + required_password);

        Button btnLogin = view.findViewById(R.id.btn_login);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputPassword = view.findViewById(R.id.inputPassword);

        //navigate from login to welcome page
       btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmLogin(required_email,required_name,required_password);
            }
        });

       //navigate from login to signUp page
        TextView lnkRegister = view.findViewById(R.id.lnk_register);
        lnkRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegistration();
            }
        });

        return view;
    }
    private void confirmLogin(String required_email, String required_name, String required_password){
        String emailInput = inputEmail.getText().toString();
        String passInput = inputPassword.getText().toString();

        if (!TextUtils.isEmpty(emailInput) && !TextUtils.isEmpty(passInput)){
            if (emailInput.contains("@")){
                loginByEmail(emailInput, passInput, required_email, required_password);
            }else{
            loginByName(emailInput, passInput, required_name, required_password);
            }
        }else {inputEmail.setError(getString(R.string.fieldEmpty_error));
            inputPassword.setError(getString(R.string.fieldEmpty_error));}
    }

    private void loginByEmail(String emailInput,String passInput,String required_email,String required_password){
        if (emailInput.equals(required_email) && !passInput.equals(required_password)){
            Toast.makeText(this.getActivity(),getString(R.string.invalid_password_error),Toast.LENGTH_LONG).show();
        }else if(emailInput.equals(required_email) && passInput.equals(required_password)){
                sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).apply();
                startWelcome();
                clearFields();
            }else {
                showAlert();
                Toast.makeText(this.getActivity(), getString(R.string.emailAddress_password_error), Toast.LENGTH_LONG).show();
            }
    }

    private void loginByName(String emailInput,String passInput,String required_name,String required_password){
        if (emailInput.equals(required_name) && !passInput.equals(required_password)){
            Toast.makeText(this.getActivity(),getString(R.string.invalid_password_error),Toast.LENGTH_LONG).show();
        }else if(emailInput.equals(required_name) && passInput.equals(required_password)){
            sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).apply();
            startWelcome();
            clearFields();
        }else {
            showAlert();
            Toast.makeText(this.getActivity(),getString(R.string.username_password_error),Toast.LENGTH_LONG).show();
        }
    }

    private void startWelcome(){
        WelcomeFragment newFragment = new WelcomeFragment();
        FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showRegistration(){
        RegistrationFragment newFragment = new RegistrationFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity())
                .setTitle(getString(R.string.registration_fragment))
                .setMessage(getString(R.string.user_not_found))
                .setNeutralButton("Register Here", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showRegistration();
                    }
                });
        builder.show();
    }

    private void clearFields(){
        inputEmail.setText(null);
        inputPassword.setText(null);
    }
}
