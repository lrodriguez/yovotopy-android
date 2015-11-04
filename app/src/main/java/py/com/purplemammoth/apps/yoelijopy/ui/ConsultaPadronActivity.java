package py.com.purplemammoth.apps.yoelijopy.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import py.com.purplemammoth.apps.yoelijopy.R;
import py.com.purplemammoth.apps.yoelijopy.util.AppConstants;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConsultaPadronActivity extends AppCompatActivity implements
        ConsultaPadronFragment.OnFragmentInteractionListener {
    private static final String TAG = "ConsultaPadron";
    private String cedula;

    private CollapsingToolbarLayout toolbarLayout;
    private TextInputLayout cedulaInput;
    private EditText cedulaText;

    private ConsultaPadronFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_padron);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = null;

        if (getIntent().getExtras() != null) {
            title = getIntent().getStringExtra(AppConstants.ARG_ACTIVITY_TITLE);
            cedula = getIntent().getStringExtra(AppConstants.ARG_CEDULA);
        }

        toolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(AppConstants.EMPTY_STRING);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            toolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        } else {
            toolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));
        }

        /*final View collapsingView = LayoutInflater.from(this).inflate(R.layout.view_collapsing_toolbar,
                null);*/
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.collapsing_container);
        cedulaInput = (TextInputLayout) findViewById(R.id.cedula_input_layout);
        cedulaText = (EditText) findViewById(R.id.cedula_edit_text);
//        toolbarLayout.addView(collapsingView);

        cedulaText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT && !validateCedula()) {
                    showErrorCedulaText(true);
                    return true;
                } else {
                    showErrorCedulaText(false);
                    return false;
                }
            }
        });

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offSet) {
                // Fix para el refresh layout
                if (fragment != null) {
                    if (offSet == 0) {
                        fragment.getRefreshLayout().setEnabled(true);
                    } else {
                        fragment.getRefreshLayout().setEnabled(false);
                    }
                }

                float collapsedRatio = (float) offSet / appBarLayout.getTotalScrollRange();
                Log.d(TAG, "collapsedRatio: " + collapsedRatio);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    linearLayout.setAlpha(1 + collapsedRatio);
                    linearLayout.setAlpha(1 + collapsedRatio);
                } else {
                    if ((1 == 1 + collapsedRatio)) {
                        linearLayout.setVisibility(View.VISIBLE);
                    } else {
                        linearLayout.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consultarPadron();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            fragment = ConsultaPadronFragment.newInstance(cedula, false);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.container, fragment, "consulta_padron_fragment")
                    .commit();
        }
    }

    private boolean validateCedula() {
        int textLength = cedulaText.getText().length();
        return textLength > AppConstants.MIN_LENGHT;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void consultarPadron() {
        if (validateCedula()) {
            // TODO como ocultar el teclado de forma definitiva
            showErrorCedulaText(false);
            showKeyboard(false);
            toolbarLayout.setTitle("Datos para " + cedulaText.getText().toString());
            fragment.performRequest(cedulaText.getText().toString());
        } else {
            showErrorCedulaText(!validateCedula());
            Snackbar.make(fragment.getView(), "Verifique los datos ingresados",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showErrorCedulaText(boolean show) {
        if (show) {
            cedulaInput.setError("Ingrese un Nro de cédula válido");
            if (!cedulaInput.isFocused()) {
                cedulaInput.requestFocus();
                showKeyboard(show);
            }
        } else {
            cedulaInput.setError(AppConstants.EMPTY_STRING);
        }
    }

    private void showKeyboard(boolean show) {
        if (show) {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
