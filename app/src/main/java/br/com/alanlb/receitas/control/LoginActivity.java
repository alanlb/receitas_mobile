package br.com.alanlb.receitas.control;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.alanlb.receitas.MainActivity;
import br.com.alanlb.receitas.R;
import br.com.alanlb.receitas.dao.ConfiguracaoFireBase;
import br.com.alanlb.receitas.dao.Facade;
import br.com.alanlb.receitas.dao.SingletonFactory;
import br.com.alanlb.receitas.dao.UsuarioDAOSqlite;
import br.com.alanlb.receitas.dao.firebase.FireBaseBD;
import br.com.alanlb.receitas.exception.SqliteException;
import br.com.alanlb.receitas.exception.ValidationException;
import br.com.alanlb.receitas.helper.Base64Custom;
import br.com.alanlb.receitas.helper.Preferencias;
import br.com.alanlb.receitas.model.Usuario;
import br.com.alanlb.receitas.model.UsuarioLogado;
import br.com.alanlb.receitas.util.Util;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Usuario u;
    private static FirebaseAuth autenticacao;
    private FirebaseAuth auth;
    private Usuario novoUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Usuario usuario = pegaUsuarioLogado(1);
        if(usuario != null){
//            Toast.makeText(this,"ENTREIIIII EXISTEEE", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this," usuario: "+ usuario.getIdFireBase()+
//                                             " login: "+usuario.getLogin()+
//                                             " senha: "+ usuario.getSenha(), Toast.LENGTH_SHORT).show();

            validarLoginFireBase(usuario);
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                Usuario u = new Usuario();
                u.setLogin(mEmailView.getText().toString());
                u.setSenha(mPasswordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Button button1 = (Button) findViewById(R.id.email_sign_in_button);
        button1.setEnabled(true);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

//         Check for a valid email address.
        u = new Usuario();
        u.setLogin(email);
        u.setSenha(password);
//        try {
//            Usuario u2 = Facade.buscarUsuarioPorLogin(this, email, password);
//
//            if (u2 != null) {
//                u.setId(u2.getId());
//                u.setNomeCompleto(u2.getNomeCompleto());
//            }else{
//                u.setId(1);
//                u.setNomeCompleto("Qualquer nome");
//                Toast.makeText(this, "E-mail ou senha inválidos", Toast.LENGTH_SHORT).show();
//            }
//        } catch (SqliteException e) {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
        validarLoginFireBase(u);


    }

    public void verificaUsuarioLogado(){

    }

    private boolean isEmailValid(String email){
        //TODO: Replace this with your own logic
        try {
            Facade.validarEmailUsuario(email);
        } catch (ValidationException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void cadastro(View view){
        setContentView(R.layout.tela_cadastro);
    }

    public void criaAuth(Usuario u){
        auth = ConfiguracaoFireBase.getFireBaseAutenticacao();
        novoUsuario = u;
        auth.createUserWithEmailAndPassword(novoUsuario.getLogin(), novoUsuario.getSenha())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getBaseContext(), "Cadastrado com sucesso!", Toast.LENGTH_LONG).show();
                            //String identificadorUsuario = Base64Custom.codificarBase64(novoUsuario.getLogin());

                            FirebaseUser usuarioFireBase = task.getResult().getUser();
                            novoUsuario.setIdFireBase(usuarioFireBase.getUid());
                            novoUsuario.salvar(getBaseContext());

                            //Preferencias preferencia = new Preferencias(LoginActivity.this);
                            //preferencia.salvarUsuarioPreferencias(u.getIdFireBase(), novoUsuario.getNomeCompleto());
                            setContentView(R.layout.activity_login);
                        }else{
                            String erro = "";

                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                erro = "Digite uma senha com no mínimo 6 caracteres";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = "O e-mail digitado é inválido";
                            }catch (FirebaseAuthUserCollisionException e){
                                erro = "E-mail já cadastrado";
                            }catch (Exception e){
                                erro = "Erro ao efetuar o cadastro";
                                e.printStackTrace();
                            }

                            Toast.makeText(getBaseContext(), erro,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void cadastrarFireBase(Usuario u){
//        try {
//            DatabaseReference reference = FireBaseBD.getReference(this);
//            reference.child("Usuario").child(u.getIdFireBase()).setValue(u);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        criaAuth(u);
    }

    public void cadastrarUsuario(View view){
        EditText nome =  (EditText)findViewById(R.id.nome_cadastro);
        EditText email =  (EditText)findViewById(R.id.email_cadastro);
        EditText senha =  (EditText)findViewById(R.id.password_cadastro);
        String nomeString = nome.getText().toString();
        String emailString = email.getText().toString();
        String senhaString = senha.getText().toString();
        try {
            Facade.validarCadastro(nomeString, emailString, senhaString);
            novoUsuario = new Usuario();
            novoUsuario.setNomeCompleto(nomeString);
            novoUsuario.setLogin(emailString);
            novoUsuario.setSenha(senhaString);
            //Facade.cadastrarUsuario(this, novoUsuario);
            cadastrarFireBase(novoUsuario);
//            setContentView(R.layout.activity_login);
            Toast.makeText(this, "Cadastrado com sucesso! ", Toast.LENGTH_SHORT).show();
        } catch (ValidationException e) {
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        }// catch (SqliteException e) {
        //    Toast.makeText(this,e.getMessage(), Toast.LENGTH_SHORT).show();
        //}

    }

    public void validarLoginFireBase(final Usuario u){
        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(u.getLogin(), u.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = autenticacao.getCurrentUser();
                    u.setId(1);
                    u.setIdFireBase(user.getUid());
                    u.setNomeCompleto(user.getEmail());
                    u.setLogin(user.getEmail());
                    u.setSenha(u.getSenha());
//                    try {
//                        Facade.cadastrarUsuario(getBaseContext(), u);
//                    } catch (SqliteException e) {
//                        e.printStackTrace();
//                    }
                    chamaTela(u);
                }
            }
        });
    }

    public void chamaTela(Usuario u){
        showProgress(true);
        mAuthTask = new UserLoginTask(u.getLogin(), u.getSenha());
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL", u.getLogin());
        bundle.putString("NOME", u.getLogin());
        bundle.putString("ID",u.getIdFireBase());
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(bundle);
        u.setSincronizado(true);
        u.setId(1);

        verificaSeUsuarioJaExiste(u);

        startActivity(intent);
        finish();
        mAuthTask.execute((Void) null);
    }

    public void verificaSeUsuarioJaExiste(Usuario u){
        try {
            Usuario usuario = pegaUsuarioLogado(u.getId());
            if(usuario == null){
                Facade.cadastrarUsuario(getBaseContext(), u);
            }else{
                Facade.deletarTabelaPorId(getBaseContext(), u.getId());
                Facade.cadastrarUsuario(getBaseContext(), u);
            }
        } catch (SqliteException e) {
            e.printStackTrace();
        }
    }

    public Usuario pegaUsuarioLogado(int id){
        try {
            return Facade.buscarUsuarioPorID(getBaseContext(), id);
        } catch (SqliteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

