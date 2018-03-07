package edu.ml.tensorflow.service.api;

import edu.ml.tensorflow.model.analyzer.Grid;
import edu.ml.tensorflow.model.analyzer.SetOfCards;
import edu.ml.tensorflow.model.recognition.Recognition;
import edu.ml.tensorflow.service.analyzer.GridAnalyzer;
import edu.ml.tensorflow.service.classifier.ObjectDetector;
import edu.ml.tensorflow.service.exception.ServiceException;
import edu.ml.tensorflow.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

@Controller
public class FileUploadController {
    private final StorageService storageService;
    private final ObjectDetector objectDetector;
    private final GridAnalyzer gridAnalyzer;

    @Autowired
    public FileUploadController(final StorageService storageService, final ObjectDetector objectDetector,
                                final GridAnalyzer gridAnalyzer) {
        this.storageService = storageService;
        this.objectDetector = objectDetector;
        this.gridAnalyzer = gridAnalyzer;
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
        String originalImagePath = "/upload-dir/" + storageService.store(file);
        Map<String, Object> result = objectDetector.detect("." + originalImagePath);
        List<Recognition> recognitionList = (List<Recognition>) result.get("recognitions");
        List<SetOfCards> validSets = gridAnalyzer.detectSet(new Grid(recognitionList));

        model.addAttribute("originalName", file.getOriginalFilename());
        model.addAttribute("originalImage", originalImagePath);
        model.addAttribute("predictedImage", result.get("labeledFilePath"));
        model.addAttribute("recognitions", recognitionList);
        model.addAttribute("validSets", validSets);
        return "display-result";
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleStorageFileNotFound(ServiceException ex) {
        return ResponseEntity.notFound().build();
    }
}
