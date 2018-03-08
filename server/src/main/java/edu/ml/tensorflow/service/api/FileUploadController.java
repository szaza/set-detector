package edu.ml.tensorflow.service.api;

import edu.ml.tensorflow.service.api.assembler.ResultAssembler;
import edu.ml.tensorflow.service.api.dto.ResultDTO;
import edu.ml.tensorflow.service.classifier.ObjectDetector;
import edu.ml.tensorflow.service.exception.ServiceException;
import edu.ml.tensorflow.service.storage.StorageService;
import edu.ml.tensorflow.util.IOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileUploadController {
    private final StorageService storageService;
    private final ObjectDetector objectDetector;
    private final ResultAssembler resultAssembler;

    @Autowired
    public FileUploadController(final StorageService storageService, final ObjectDetector objectDetector,
                                final ResultAssembler resultAssembler) {
        this.storageService = storageService;
        this.objectDetector = objectDetector;
        this.resultAssembler = resultAssembler;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        return "upload-image";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        byte[] image = IOUtil.readAllBytesOrExit(storageService.store(file));
        ResultDTO resultDTO = resultAssembler.convertToDTO(objectDetector.detect(image), image);
        model.addAttribute("originalName", file.getOriginalFilename());
        model.addAttribute("result", resultDTO);
        return "display-result";
    }

    @PostMapping(value = "/mobile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultDTO> handleFileUpload(@RequestParam("file") MultipartFile file) {
        byte[] image = IOUtil.readAllBytesOrExit(storageService.store(file));
        return ResponseEntity.ok().body(resultAssembler.convertToDTO(objectDetector.detect(image), image));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ServiceException ex) {
        return ResponseEntity.notFound().build();
    }
}
