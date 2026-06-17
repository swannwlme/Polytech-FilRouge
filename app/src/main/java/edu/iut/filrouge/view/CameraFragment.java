package edu.iut.filrouge.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;

import edu.iut.filrouge.R;
import edu.iut.filrouge.controller.Picturable;

public class CameraFragment extends Fragment {

    public static final String REQUEST_DISPLAY_PICTURE = "camera_display_picture";
    public static final String BUNDLE_PICTURE_PATH = "picture_path";

    private static final String KEY_DISPLAYED_PICTURE_PATH = "displayed_picture_path";
    private static final String KEY_PENDING_PICTURE_PATH = "pending_picture_path";

    private Picturable picturable;
    private ImageView photoPreview;
    private String displayedPicturePath;
    private String pendingPicturePath;

    private final ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            granted -> {
                if (granted) {
                    launchCamera();
                } else {
                    showPermissionDeniedDialog();
                }
            }
    );

    private final ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success && pendingPicturePath != null) {
                    displayPicture(pendingPicturePath);

                    if (picturable != null) {
                        picturable.onPictureTaken(pendingPicturePath);
                    }
                } else {
                    Toast.makeText(requireContext(), R.string.camera_capture_failed, Toast.LENGTH_SHORT).show();
                }

                pendingPicturePath = null;
            }
    );

    public CameraFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof Picturable) {
            picturable = (Picturable) context;
        } else {
            throw new IllegalStateException(context + " doit implémenter Picturable");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            displayedPicturePath = savedInstanceState.getString(KEY_DISPLAYED_PICTURE_PATH);
            pendingPicturePath = savedInstanceState.getString(KEY_PENDING_PICTURE_PATH);
        }

        getParentFragmentManager().setFragmentResultListener(
                REQUEST_DISPLAY_PICTURE,
                this,
                (requestKey, bundle) -> displayPicture(bundle.getString(BUNDLE_PICTURE_PATH))
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photoPreview = view.findViewById(R.id.photoPreview);
        view.findViewById(R.id.takePictureButton).setOnClickListener(v -> requestCameraAccess());

        displayPicture(displayedPicturePath);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_DISPLAYED_PICTURE_PATH, displayedPicturePath);
        outState.putString(KEY_PENDING_PICTURE_PATH, pendingPicturePath);
    }

    @Override
    public void onDestroyView() {
        photoPreview = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        picturable = null;
        super.onDetach();
    }

    private void requestCameraAccess() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            launchCamera();
            return;
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showPermissionRationaleDialog();
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void showPermissionRationaleDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.camera_permission_title)
                .setMessage(R.string.camera_permission_message)
                .setPositiveButton(R.string.camera_permission_positive,
                        (dialog, which) -> permissionLauncher.launch(Manifest.permission.CAMERA))
                .setNegativeButton(R.string.camera_permission_negative, null)
                .show();
    }

    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.camera_permission_title)
                .setMessage(R.string.camera_permission_denied)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private void launchCamera() {
        Uri photoUri = createPhotoUri();

        if (photoUri == null) {
            return;
        }

        pendingPicturePath = photoUri.toString();

        try {
            takePictureLauncher.launch(photoUri);
        } catch (Exception e) {
            pendingPicturePath = null;
            Toast.makeText(requireContext(), R.string.camera_capture_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private Uri createPhotoUri() {
        try {
            File photoFile = File.createTempFile(
                    "incident_photo_",
                    ".jpg",
                    requireContext().getCacheDir()
            );
            String authority = requireContext().getPackageName() + ".fileprovider";
            return FileProvider.getUriForFile(requireContext(), authority, photoFile);
        } catch (IOException e) {
            Toast.makeText(requireContext(), R.string.camera_file_error, Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void displayPicture(@Nullable String picturePath) {
        displayedPicturePath = picturePath;

        if (photoPreview == null) {
            return;
        }

        if (picturePath == null || picturePath.trim().isEmpty()) {
            photoPreview.setImageResource(R.drawable.photo);
            return;
        }

        photoPreview.setImageURI(null);
        photoPreview.setImageURI(Uri.parse(picturePath));
    }
}
