package py.com.purpleapps.yovotopy.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.loopj.android.http.Base64;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import py.com.purpleapps.yovotopy.R;
import py.com.purpleapps.yovotopy.client.EleccionesRestCallback;
import py.com.purpleapps.yovotopy.model.AvizorCategory;
import py.com.purpleapps.yovotopy.model.AvizorCategoryWrapper;
import py.com.purpleapps.yovotopy.model.DatosConsultaPadron;
import py.com.purpleapps.yovotopy.model.DatosDenuncia;
import py.com.purpleapps.yovotopy.model.FotoDenuncia;
import py.com.purpleapps.yovotopy.util.ImageFilePath;
import py.com.purpleapps.yovotopy.util.Tracking;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DenunciasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DenunciasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DenunciasFragment extends Fragment implements EleccionesRestCallback.OnResponseReceived {
    public static final String TAG = "Denuncias";
    public static final int PLACE_PICKER_REQUEST = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int ACTION_TAKE_PHOTO = 10;
    private static final int PICK_FROM_GALLERY = 100;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.links_container)
    LinearLayout linksContainer;
    // Form required fields
    @Bind(R.id.titulo_text_input)
    TextInputLayout tituloTextInput;
    @Bind(R.id.descripcion_text_input)
    TextInputLayout descripcionTextInput;
    @Bind(R.id.fecha_text_input)
    TextInputLayout fechaTextInput;
    @Bind(R.id.hora_text_input)
    TextInputLayout horaTextLayout;
    @Bind(R.id.categorias_text_input)
    TextInputLayout categoriasTextInput;
    @Bind(R.id.ubicacion_text_input)
    TextInputLayout ubicacionTextInput;
    @Bind(R.id.lugar_text_input)
    TextInputLayout lugarTextInput;
    @Bind(R.id.titulo_text)
    EditText titulo;
    @Bind(R.id.descripcion_text)
    EditText descripcion;
    @Bind(R.id.fecha_text)
    EditText fecha;
    @Bind(R.id.hora_text)
    EditText hora;
    @Bind(R.id.categorias_text)
    EditText categoria;
    @Bind(R.id.ubicacion_text)
    EditText ubicacion;
    @Bind(R.id.lugar_text)
    EditText lugar;
    //Form optional fields
    @Bind(R.id.nombre_text)
    EditText nombre;
    @Bind(R.id.apellido_text)
    EditText apellido;
    @Bind(R.id.email_text)
    EditText email;
    @Bind(R.id.telefono_text)
    EditText telefono;
    @Bind(R.id.add_link_button)
    Button addLink;
    @Bind(R.id.add_foto_button)
    Button addFoto;
    @Bind(R.id.camera_image)
    ImageView fotoDenuncia;
    @Bind(R.id.remove_image)
    ImageView removeFoto;
    @BindString(R.string.error_input_required)
    String errorRequired;
    private OnFragmentInteractionListener mListener;
    // Rest callback variables
    private EleccionesRestCallback restCallback;
    private ArrayList<AvizorCategory> avizorCategories;
    // Views
    private View parentView;
    // Variables
    private boolean isValid = false;
    private String tituloText;
    private String descripcionText;
    private String fechaText;
    private String horaText;
    private String[] idsCategorias;
    private String[] categorias;
    private boolean[] selectedCategories;
    private double latitude;
    private double longitude;
    private String lugarText;
    private String nombreText;
    private String apellidoText;
    private String emailText;
    private String telefonoText;
    private ArrayList<String> links;
    private String imagePath;

    public DenunciasFragment() {
        // Required empty public constructor
    }

    public static DenunciasFragment newInstance() {
        DenunciasFragment fragment = new DenunciasFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Elecciones Py 2015");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Elecciones Py 2015", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        links = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        tituloText = titulo.getText().toString();
        descripcionText = descripcion.getText().toString();
        fechaText = fecha.getText().toString();
        horaText = hora.getText().toString();
        String categoriasText = categoria.getText().toString();
        String ubicacionText = ubicacion.getText().toString();
        lugarText = lugar.getText().toString();
        nombreText = nombre.getText().toString();
        apellidoText = apellido.getText().toString();
        emailText = email.getText().toString();
        telefonoText = telefono.getText().toString();

        outState.putString("titulo_text", tituloText);
        outState.putString("descripcion_text", descripcionText);
        outState.putString("fecha_text", fechaText);
        outState.putString("hora_text", horaText);
        outState.putString("categorias_text", categoriasText);
        outState.putString("ubicacion_text", ubicacionText);
        outState.putString("lugar_text", lugarText);
        outState.putString("nombre_text", nombreText);
        outState.putString("apellido_text", apellidoText);
        outState.putString("email_text", emailText);
        outState.putString("telefono_text", telefonoText);

        outState.putStringArray("ids_categorias", idsCategorias);
        outState.putStringArray("nombres_categorias", categorias);
        outState.putBooleanArray("selected_categorias", selectedCategories);

        outState.putDouble("latitud", latitude);
        outState.putDouble("longitud", longitude);

        outState.putStringArrayList("links_array", links);

        outState.putString("image_path", imagePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_denuncias, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = view;

        refreshLayout.setEnabled(false);

        fecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        hora.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePickerDialog();
                }
            }
        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        categoria.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (categorias == null && selectedCategories == null) {
                        performRequest();
                    } else {
                        showCategoriesDialog();
                    }
                }
            }
        });

        categoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categorias == null && selectedCategories == null) {
                    performRequest();
                } else {
                    showCategoriesDialog();
                }
            }
        });

        ubicacion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showPlacesPicker();
                }
            }
        });

        ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacesPicker();
            }
        });

        addLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracking.track(getActivity().getApplication(),
                        Tracking.Pantalla.DENUNCIAS,
                        Tracking.Accion.ANHADIR_LINKS);
                showAddLinkDialog();
            }
        });

        addFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracking.track(getActivity().getApplication(),
                        Tracking.Pantalla.DENUNCIAS,
                        Tracking.Accion.ANHADIR_FOTO);
                showAddPhotoDialog();
            }
        });

        removeFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture();
            }
        });

        restCallback = new EleccionesRestCallback(this, getActivity(), refreshLayout, parentView);

        if (savedInstanceState != null) {
            tituloText = savedInstanceState.getString("titulo_text");
            descripcionText = savedInstanceState.getString("descripcion_text");
            fechaText = savedInstanceState.getString("fecha_text");
            horaText = savedInstanceState.getString("hora_text");
            String categoriasText = savedInstanceState.getString("categorias_text");
            String ubicacionText = savedInstanceState.getString("ubicacion_text");
            lugarText = savedInstanceState.getString("lugar_text");
            nombreText = savedInstanceState.getString("nombre_text");
            apellidoText = savedInstanceState.getString("apellido_text");
            emailText = savedInstanceState.getString("email_text");
            telefonoText = savedInstanceState.getString("telefono_text");

            idsCategorias = savedInstanceState.getStringArray("ids_categorias");
            categorias = savedInstanceState.getStringArray("nombres_categorias");
            selectedCategories = savedInstanceState.getBooleanArray("selected_categorias");

            latitude = savedInstanceState.getDouble("latitud");
            longitude = savedInstanceState.getDouble("longitud");

            links = savedInstanceState.getStringArrayList("links_array");

            imagePath = savedInstanceState.getString("image_path");

            titulo.setText(tituloText);
            descripcion.setText(descripcionText);
            fecha.setText(fechaText);
            hora.setText(horaText);
            categoria.setText(categoriasText);
            ubicacion.setText(ubicacionText);
            lugar.setText(lugarText);
            nombre.setText(nombreText);
            apellido.setText(apellidoText);
            email.setText(emailText);
            telefono.setText(telefonoText);

            Log.d(TAG, links.toString());

            ArrayList<String> linksTemp = new ArrayList<>(links);
            links.clear();

            if (linksContainer != null && linksContainer.getChildCount() == 0) {
                for (String link : linksTemp) {
                    addLinksView(link);
                }
            }

            if (imagePath != null && !imagePath.isEmpty()) {
                loadPicture(imagePath);
            }
        }

        Tracking.track(getActivity().getApplication(),
                Tracking.Pantalla.DENUNCIAS,
                Tracking.Accion.VER_PANTALLA);
    }

    // Dialogs

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, getActivity());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    ubicacion.setText(String.format("%s", (latitude + "; "
                            + longitude)));
                    lugar.setText(place.getName() + " - " + place.getAddress());
                }
                break;
            // TODO verificar tamaño de archivo
            case ACTION_TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Path de la imagen: " + imagePath);
                    loadPicture(imagePath);
                    galleryAddPic();
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    imagePath = "";
                }
                break;
            case PICK_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "Content path de la imagen" + data.getData());
                    imagePath = ImageFilePath.getPath(getActivity().getApplicationContext(), data.getData());
                    Log.i("Image File Path", "" + data.getData());
                    loadPicture(imagePath);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    imagePath = "";
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void showDatePickerDialog() {
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        showKeyboard(false);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String fechaText = String.format("%02d/%02d/%d", dayOfMonth, (monthOfYear + 1), year);
                fecha.setText(fechaText);
                hora.requestFocus();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Date fechaActual = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaActual);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        showKeyboard(false);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String horaText = String.format("%02d:%02d", hourOfDay, minute);
                hora.setText(horaText);
                categoria.requestFocus();
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void showCategoriesDialog() {
        showKeyboard(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog)
                .setMultiChoiceItems(categorias, selectedCategories,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selectedCategories[which] = isChecked;
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String categoriasText = "";
                        for (int i = 0; i < categorias.length; i++) {
                            if (selectedCategories[i]) {
                                categoriasText = categoriasText + categorias[i] + "\n";
                            }
                        }
                        if (!categoriasText.isEmpty()) {
                            categoria.setText(categoriasText.substring(0, (categoriasText.length() - 1)));
                        }
                        ubicacion.requestFocus();
                    }
                });
        builder.show();
    }

    // View uodate methods

    private void showPlacesPicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Context context = getActivity();
        try {
            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void showAddLinkDialog() {
        if (links.size() >= 10) {
            Snackbar.make(parentView, "Ya sobrepasaste la cantidad máxima de links por denuncia",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_datos_consulta, null);

        final EditText linkText = (EditText) dialogView.findViewById(R.id.link_edit_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog)
                .setView(dialogView)
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String link = linkText.getText().toString();
                        if (!link.isEmpty()) {
                            addLinksView(link);
                        }
                    }
                })
                .setNegativeButton("Cancelar", null);

        builder.show();
    }

    private void showAddPhotoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog)
                .setItems(new CharSequence[]{"Usar la cámara",
                        "Añadir desde la galería"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            takePictureIntent(ACTION_TAKE_PHOTO);
                        } else if (which == 1) {
                            pickFromGalleryIntent(PICK_FROM_GALLERY);
                        }
                    }
                });
        builder.show();
    }

    private void addLinksView(final String link) {
        links.add(link);

        final View linksView = LayoutInflater.from(getActivity()).inflate(R.layout.content_links_view, null);
        TextView textView = (TextView) ((RelativeLayout) linksView).getChildAt(0);
        ImageView removeButton = (ImageView) ((RelativeLayout) linksView).getChildAt(1);

        textView.setText(link);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linksContainer.removeView(linksView);
                links.remove(link);
            }
        });

        linksContainer.addView(linksView);
    }

    private void removeLinksView() {

    }

    public void takePictureIntent(int actionId) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        imagePath = fileUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, actionId);
    }

    private void pickFromGalleryIntent(int actionId) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, actionId);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void loadPicture(String path) {
        File file = new File(path);
        if (!file.exists()) {
            imagePath = "";
            Snackbar.make(parentView, "Ocurrió un error al adjuntar la imagen, intente nuevamente", Snackbar.LENGTH_LONG).show();
            return;
        }

        imagePath = path;
        Glide.with(this).load(new File(imagePath))
                .thumbnail(0.1f).into(fotoDenuncia);
        addFoto.setVisibility(View.GONE);
        fotoDenuncia.setVisibility(View.VISIBLE);
        removeFoto.setVisibility(View.VISIBLE);
    }

    private void deletePicture() {
        if (imagePath != null && !imagePath.isEmpty()) {
            final String previousImagePath = imagePath;
            imagePath = "";
            addFoto.setVisibility(View.VISIBLE);
            fotoDenuncia.setVisibility(View.GONE);
            removeFoto.setVisibility(View.GONE);
            fotoDenuncia.setImageDrawable(getResources()
                    .getDrawable(android.R.drawable.ic_menu_camera));
            Snackbar.make(parentView, "Foto eliminada", Snackbar.LENGTH_SHORT)
                    .setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadPicture(previousImagePath);
                        }
                    }).show();
        }
    }

    public void validateForm() {
        tituloText = titulo.getText().toString();
        descripcionText = descripcion.getText().toString();
        fechaText = fecha.getText().toString();
        horaText = hora.getText().toString();
        String categoriasText = categoria.getText().toString();
        String ubicacionText = ubicacion.getText().toString();
        lugarText = lugar.getText().toString();
        nombreText = nombre.getText().toString();
        apellidoText = apellido.getText().toString();
        emailText = email.getText().toString();
        telefonoText = telefono.getText().toString();

        isValid = true;

        showError(tituloTextInput, tituloText);
        showError(descripcionTextInput, descripcionText);
        showError(fechaTextInput, fechaText);
        showError(horaTextLayout, horaText);
        showError(categoriasTextInput, categoriasText);
        showError(ubicacionTextInput, ubicacionText);
        showError(lugarTextInput, lugarText);

        String base64String = null, fileName = null, fileType = null;
        try {
            if (!imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                long fileSize = FileUtils.sizeOf(imageFile);
                if (fileSize > (1 * 10E7)) {
                    Snackbar.make(parentView, "La imagen pesa más de 10 Mb, " +
                            "cargue una imagen de menor tamaño", Snackbar.LENGTH_LONG).show();
                    return;
                }
                base64String = Base64
                        .encodeToString(FileUtils.readFileToByteArray(imageFile),
                                Base64.DEFAULT);
                fileName = imageFile.getName();
                fileType = getMimeType(imagePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Categorías
        List<String> selectedIdCategorias = new ArrayList<>();

        for (int i = 0; i < selectedCategories.length; i++) {
            if (selectedCategories[i]) {
                selectedIdCategorias.add(idsCategorias[i]);
            }
        }

        List<FotoDenuncia> fotoDenunciaList = new ArrayList<>();
        // Fotos
        if (fileName != null && fileType != null && base64String != null) {
            FotoDenuncia fotoDenuncia = new FotoDenuncia();
            fotoDenuncia.setNombre(fileName);
            fotoDenuncia.setFoto(base64String);
            fotoDenuncia.setMediaType(fileType);

            fotoDenunciaList.add(fotoDenuncia);
        }

        DatosDenuncia datosDenuncia = new DatosDenuncia();
        datosDenuncia.setTitulo(tituloText);
        datosDenuncia.setDescripcion(descripcionText);
        datosDenuncia.setFecha(fechaText);
        datosDenuncia.setHora(horaText);
        datosDenuncia.setCategorias(selectedIdCategorias);
        datosDenuncia.setLatitud(Double.toString(latitude));
        datosDenuncia.setLongitud(Double.toString(longitude));
        datosDenuncia.setLugar(lugarText);
        datosDenuncia.setNombre(nombreText);
        datosDenuncia.setApellido(apellidoText);
        datosDenuncia.setEmail(emailText);
        datosDenuncia.setTelefono(telefonoText);
        datosDenuncia.setLinks(links);
        datosDenuncia.setFotos(fotoDenunciaList);

        Log.e(TAG, datosDenuncia.toString());

        if (isValid) {
            try {
                restCallback.postDenuncia(datosDenuncia);
                Tracking.track(getActivity().getApplication(),
                        Tracking.Pantalla.DENUNCIAS,
                        Tracking.Accion.ENVIAR_DENUNCIA);
            } catch (JSONException e) {
                Log.e(TAG, "Ocurrió un error al enviar la denuncia: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    private void showError(TextInputLayout textInputLayout, String text) {
        if (text.isEmpty()) {
            textInputLayout.setError(errorRequired);
            isValid = false;
        } else {
            textInputLayout.setError("");
        }
    }

    private void showKeyboard(boolean show) {
        if (show) {
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            ((InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }

    }

    public void performRequest() {
        try {
            restCallback.getCategoriasAvizor();
        } catch (JSONException e) {
            Log.e(TAG, "Ocurrió un error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private void clearFormData() {
        tituloText = "";
        descripcionText = "";
        fechaText = "";
        horaText = "";
        lugarText = "";
        nombreText = "";
        apellidoText = "";
        emailText = "";
        telefonoText = "";

        for (int i = 0; i < selectedCategories.length; i++) {
            selectedCategories[i] = false;
        }

        latitude = -1;
        longitude = -1;

        linksContainer.removeAllViews();
        links.clear();

        titulo.setText(tituloText);
        descripcion.setText(descripcionText);
        fecha.setText(fechaText);
        hora.setText(horaText);
        categoria.setText("");
        ubicacion.setText("");
        lugar.setText(lugarText);
        nombre.setText(nombreText);
        apellido.setText(apellidoText);
        email.setText(emailText);
        telefono.setText(telefonoText);

        removeFoto.setVisibility(View.GONE);
        fotoDenuncia.setVisibility(View.GONE);
        addFoto.setVisibility(View.VISIBLE);

        imagePath = "";

        isValid = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSuccessAction(DatosConsultaPadron datosConsultaPadron) {

    }

    @Override
    public void onSuccessAction(List<AvizorCategoryWrapper> categoryList) {
        idsCategorias = new String[categoryList.size()];
        categorias = new String[categoryList.size()];
        selectedCategories = new boolean[categoryList.size()];


        for (int i = 0; i < categoryList.size(); i++) {
            idsCategorias[i] = categoryList.get(i).getAvizorCategory().getId();
            categorias[i] = categoryList.get(i).getAvizorCategory().getNombre();
            selectedCategories[i] = false;
        }

        showCategoriesDialog();
    }

    @Override
    public void onSuccessAction() {
        Snackbar.make(parentView, "Se enviaron correctamente los datos de la denuncia",
                Snackbar.LENGTH_LONG).show();
        // Borrar los datos
        clearFormData();
    }

    @Override
    public void onFailureAction(int status) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
