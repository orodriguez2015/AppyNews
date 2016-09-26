package com.appynew.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
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

import com.appynew.activities.dialog.AlertDialogHelper;
import com.appynews.controllers.OrigenRssController;
import com.appynews.exceptions.UpdateOrigenRssException;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.ConnectionUtils;

import java.util.ArrayList;
import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * A login screen that offers login via email/password.
 */
public class EditarFuenteDatosActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

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
    private AutoCompleteTextView edicionNombreOrigenRss;
    private EditText edicionUrlOrigenRss;
    private View mProgressView;
    private View mLoginFormView;
    private OrigenNoticiaVO origenRss = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_fuente_datos);
        // Set up the login form.
        edicionNombreOrigenRss = (AutoCompleteTextView) findViewById(R.id.edicionNombreOrigenRss);
        //populateAutoComplete();

        edicionUrlOrigenRss = (EditText) findViewById(R.id.edicionUrlOrigenRss);
        edicionUrlOrigenRss.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    validarDatos();
                    return true;
                }
                return false;
            }
        });

        Button buttonAceptar = (Button) findViewById(R.id.aceptarEdicionFuenteDatos);
        buttonAceptar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatos();
            }
        });


        Button buttonCancelar = (Button)findViewById(R.id.cancelarEdicionFuenteDatos);
        buttonCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        setTitle(getString(R.string.titulo_edicion_fuente_datos_rss));
        cargarFormulario();
    }


    /**
     * Método que se encarga de cargar el formulario con los datos del orígen/fuentes de datos
     * a editar
     */
    private void cargarFormulario() {
        Bundle parametros = getIntent().getExtras();
        this.origenRss = (OrigenNoticiaVO)parametros.get("origenRss");

        this.edicionNombreOrigenRss.setText(origenRss.getNombre());
        this.edicionUrlOrigenRss.setText(origenRss.getUrl());
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void validarDatos() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edicionNombreOrigenRss.setError(null);
        edicionUrlOrigenRss.setError(null);

        // Store values at the time of the login attempt.
        String nombre    = edicionNombreOrigenRss.getText().toString();
        String url = edicionUrlOrigenRss.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Chequeo de nombre y de url
        if (TextUtils.isEmpty(nombre)) {
            edicionNombreOrigenRss.setError(getString(R.string.campo_obligatorio));
            focusView = edicionNombreOrigenRss;
            cancel = true;
        }
        else
        if (TextUtils.isEmpty(url)) {
            edicionUrlOrigenRss.setError(getString(R.string.campo_obligatorio));
            focusView = edicionUrlOrigenRss;
            cancel = true;
        }
        else
        if (!ConnectionUtils.isUrlFormatoValida(url)) {
            edicionUrlOrigenRss.setError(getString(R.string.err_formato_url_origen));
            focusView = edicionUrlOrigenRss;
            cancel = true;
        } else {

            int isOnline = ConnectionUtils.isOnline(this,url);

            switch(isOnline) {
                case 0: // Todo OK
                    cancel = false;
                    break;

                case 1: // No se ha podido establecer conexión con la url del recurso RSS
                    this.edicionUrlOrigenRss.setError(getString(R.string.err_conexion_url_origen));
                    focusView = edicionUrlOrigenRss;
                    cancel    = true;
                    break;

                case 2: // El dispositivo no tiene habilitadas sus conexiones de red
                    this.edicionUrlOrigenRss.setError(getString(R.string.err_connection_state));
                    focusView = edicionUrlOrigenRss;
                    cancel    = true;
                    break;

            }// switch

        }
        

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);

            try {
                OrigenRssController controller = new OrigenRssController(this);
                origenRss.setNombre(this.edicionNombreOrigenRss.getText().toString());
                origenRss.setUrl(this.edicionUrlOrigenRss.getText().toString());
                controller.updateOrigenRss(origenRss);

                Intent data = new Intent();
                setResult(RESULT_OK, data);
                finish();

            } catch (UpdateOrigenRssException e) {
                e.printStackTrace();
                AlertDialogHelper.crearDialogoAlertaAdvertencia(this, getString(R.string.atencion), getString(R.string.err_update_origen_rss));
            }
        }
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
                new ArrayAdapter<>(EditarFuenteDatosActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        edicionNombreOrigenRss.setAdapter(adapter);
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
                edicionUrlOrigenRss.setError(getString(R.string.error_incorrect_password));
                edicionUrlOrigenRss .requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

