package com.tiendafunciona.service;

import com.google.auth.Credentials;
import com.google.auth.ServiceAccountSigner;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.cloud.storage.Storage.SignUrlOption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FirebaseStorageService {

    // 🔹 Nombre del bucket en Firebase Storage (ajústalo con tu ID real)
    // Ejemplo: "mi-proyecto-12345.appspot.com"
    private final String bucketName = "tu-proyecto-id.appspot.com";

    // 🔹 Carpeta principal en tu bucket donde se guardarán las imágenes
    private final String rutaSuperiorStorage = "tiendafunciona";

    // 🔹 Carpeta en resources donde está el JSON de configuración
    private final String rutaJsonFile = "firebase";

    // 🔹 Nombre del archivo JSON (sin la ruta, solo el nombre)
    private final String archivoJsonFile = "firebase-config.json";

    /**
     * Carga una imagen a Firebase Storage y devuelve su URL firmada.
     *
     * @param archivoLocalCliente archivo recibido del formulario
     * @param carpeta subcarpeta dentro del bucket (por ejemplo "productos")
     * @param id identificador del producto
     * @return URL pública de la imagen subida
     */
    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id) {
        try {
            String nombreOriginal = archivoLocalCliente.getOriginalFilename();
            String fileName = "img" + sacaNumero(id) + "_" + nombreOriginal;

            File file = convertToFile(archivoLocalCliente);
            String url = uploadFile(file, carpeta, fileName);
            file.delete(); // elimina el temporal

            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String uploadFile(File file, String carpeta, String fileName) throws IOException {
        ClassPathResource json = new ClassPathResource(rutaJsonFile + File.separator + archivoJsonFile);

        BlobId blobId = BlobId.of(bucketName, rutaSuperiorStorage + "/" + carpeta + "/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        Credentials credentials = GoogleCredentials.fromStream(json.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return storage.signUrl(blobInfo, 3650, TimeUnit.DAYS, 
                SignUrlOption.signWith((ServiceAccountSigner) credentials)).toString();
    }

    private File convertToFile(MultipartFile archivoLocalCliente) throws IOException {
        File tempFile = File.createTempFile("img", null);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(archivoLocalCliente.getBytes());
        }
        return tempFile;
    }

    private String sacaNumero(Long id) {
        return String.format("%014d", id);
    }
}
